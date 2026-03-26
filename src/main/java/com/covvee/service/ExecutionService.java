package com.covvee.service;

import com.covvee.dto.ExecutionResult;
import com.covvee.entity.Project;
import com.covvee.execption.ResourceNotFoundException;
import com.covvee.repository.ProjectRepository;
import com.covvee.service.interfaces.ExecutionInterface;
import com.covvee.utils.ProjectMaterializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ExecutionService implements ExecutionInterface {

    private final ProjectMaterializer projectMaterializer;
    private final DockerService dockerService;
    private final ProjectRepository projectRepository;

    @SneakyThrows
    @Override
    public ExecutionResult runProject(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->  new ResourceNotFoundException("project was not found"));

        Path tempDir = null;

        try {
            // 1. Create a temp folder
            tempDir = Files.createTempDirectory("covvee_run_" + projectId + "_");

            // 2. MAGIC HAPPENS HERE: Database -> Real Disk
            projectMaterializer.materializeProject(projectId, tempDir);

            // 3. Now 'tempDir' is a perfect copy of the project. Execute it!
            // Notice we are returning the actual result here instead of null
            ExecutionResult result = dockerService.execute(tempDir, project.getLanguage());

            return dockerService.execute(tempDir, project.getLanguage());

        } finally {
            // 4. CLEANUP (Crucial!)
            // This 'finally' block guarantees the folder is deleted even if the code above crashes.
            if (tempDir != null) {
                try {
                    FileSystemUtils.deleteRecursively(tempDir);
                } catch (IOException e) {
                    // Log the error so you know a file got stuck, but don't crash the app
                    System.err.println("Failed to delete temp directory: " + tempDir.toAbsolutePath());
                }
            }
        }
    }


    @Override
    public void stopExecution(String executionId) {

    }

    @Override
    public boolean isEnvironmentReady() {
        return false;
    }
}
