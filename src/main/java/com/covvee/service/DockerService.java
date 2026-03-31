package com.covvee.service;

import com.covvee.dto.ExecutionResult;
import com.covvee.enums.Language;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DockerService {
    private final DockerClient dockerClient;
    @PostConstruct
    public void init() {

        // Wrap each pull in its own try-catch so one failure doesn't stop the others
        pullImageSafely("node:18-alpine");
        pullImageSafely("python:3.9-slim");
        pullImageSafely("eclipse-temurin:17-jdk-alpine");
    }

    private void pullImageSafely(String imageTag) {
        try {
            dockerClient.pullImageCmd(imageTag)
                    .exec(new PullImageResultCallback())
                    .awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public ExecutionResult execute(Path hostSourcePath, Language language) {

        String image = getImageForLanguage(language);
        String entryPoint = getEntryPointForLanguage(language);
        // 1. Configure the Container
        HostConfig hostConfig = new HostConfig()
                .withBinds(new Bind(hostSourcePath.toAbsolutePath().toString(), new Volume("/app"))) // Mount folder
                .withMemory(128 * 1024 * 1024L) // Limit RAM to 128MB
                .withCpuQuota(50000L); // Limit CPU to 0.5 cores
        try {
            // 2. Create Container
            CreateContainerResponse container = dockerClient.createContainerCmd(image)
                    .withHostConfig(hostConfig)
                    .withWorkingDir("/app") // Run commands inside the mounted folder
                    .withCmd(getCommand(language, entryPoint))
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();

            String containerId = container.getId();

            // 3. Start Container
            dockerClient.startContainerCmd(containerId).exec();

            WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
            dockerClient.waitContainerCmd(containerId).exec(resultCallback);

            boolean completed = false;
            try {
                completed = resultCallback.awaitCompletion(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // Thread interrupted
            }

            // 5. Capture Output (Logs)
            String logs = getContainerLogs(containerId);

            // 6. Check if it timed out
            if (!completed) {
                dockerClient.killContainerCmd(containerId).exec();
                return ExecutionResult.builder()
                        .output(logs)
                        .error("Execution Timed Out (Max 60s)") // Updated error message
                        .isTimeout(true)
                        .build();
            }

            // 7. Cleanup (Remove the container, but the /tmp folder is handled by the caller)
            dockerClient.removeContainerCmd(containerId).exec();

            return ExecutionResult.builder()
                    .output(logs)
                    .exitCode(0) // You can inspect resultCallback for real exit code
                    .build();

        } catch (Exception e) {
            return ExecutionResult.builder().error("Docker Error: " + e.getMessage()).build();
        }
    }

    // --- Helpers ---

    private String getContainerLogs(String containerId) throws InterruptedException {
        StringBuilder logBuilder = new StringBuilder();
        dockerClient.logContainerCmd(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .exec(new com.github.dockerjava.api.async.ResultCallback.Adapter<com.github.dockerjava.api.model.Frame>() {
                    @Override
                    public void onNext(com.github.dockerjava.api.model.Frame object) {
                        logBuilder.append(new String(object.getPayload()));
                    }
                }).awaitCompletion();
        return logBuilder.toString();
    }

    private String getImageForLanguage(Language language) {
        return switch (language) {
            case PYTHON -> "python:3.9-slim"; // Lightweight Python
            case JAVASCRIPT -> "node:18-alpine"; // Lightweight Node
            case JAVA -> "eclipse-temurin:17-jdk-alpine";
            default -> throw new IllegalArgumentException("Unsupported language");
        };
    }

    private String getEntryPointForLanguage(Language language) {
        // Convention: User must have main.py or index.js at root
        return switch (language) {
            case PYTHON -> "main.py";
            case JAVASCRIPT -> "index.js";
            case JAVA -> "Main.java";
            default -> "main";
        };
    }

    private String[] getCommand(Language lang, String entryPoint) {
        return switch (lang) {

            case PYTHON ->
                    new String[]{"sh", "-c", "python " + entryPoint};

            case JAVASCRIPT ->
                // 🔥 Runs npm install to download their dependencies, then executes the file
                    new String[]{"sh", "-c", "npm install && node " + entryPoint};

            case JAVA -> {
                String className = entryPoint.replace(".java", "");
                yield new String[]{"sh", "-c", "javac $(find . -name \"*.java\") && java " + className};
            }

            default -> new String[]{};
        };
    }
}