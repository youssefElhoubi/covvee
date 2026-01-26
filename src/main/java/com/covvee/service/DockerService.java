package com.covvee.service;

import com.covvee.dto.ExecutionResult;
import com.covvee.enums.Language;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DockerService {
    private final DockerClient dockerClient;

    /**
     * @param hostSourcePath The path on YOUR server (/tmp/covvee_run_123)
     * @param language The project language (PYTHON, JAVASCRIPT)
     */
    public ExecutionResult execute(Path hostSourcePath, Language language) {

        String image = getImageForLanguage(language);
        String entryPoint = getEntryPointForLanguage(language); // e.g., "main.py" or "index.js"

        // 1. Configure the Container
        HostConfig hostConfig = new HostConfig()
                .withBinds(new Bind(hostSourcePath.toAbsolutePath().toString(), new Volume("/app"))) // Mount folder
                .withMemory(128 * 1024 * 1024L) // Limit RAM to 128MB (Safety!)
                .withCpuQuota(50000L) // Limit CPU to 0.5 cores
                .withNetworkMode("none"); // Disable Internet (Security!)

        try {
            // 2. Create Container
            CreateContainerResponse container = dockerClient.createContainerCmd(image)
                    .withHostConfig(hostConfig)
                    .withWorkingDir("/app") // Run commands inside the mounted folder
                    .withCmd(getCommand(language, entryPoint)) // e.g., ["python", "main.py"]
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();

            String containerId = container.getId();

            // 3. Start Container
            dockerClient.startContainerCmd(containerId).exec();

            // 4. Wait for it to finish (Max 10 seconds timeout)
            // This blocks the thread until execution is done.
            WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
            dockerClient.waitContainerCmd(containerId).exec(resultCallback);

            boolean completed = false;
            try {
                completed = resultCallback.awaitCompletion(10, TimeUnit.SECONDS);
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
                        .error("Execution Timed Out (Max 10s)")
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
            case JAVA -> "openjdk:17-slim";
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
            case PYTHON -> new String[]{"python", entryPoint};
            case JAVASCRIPT -> new String[]{"node", entryPoint};
            case JAVA -> new String[]{"java", entryPoint}; // Simplified for single-file java
            default -> new String[]{};
        };
    }
}
