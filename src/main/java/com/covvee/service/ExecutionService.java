package com.covvee.service;

import com.covvee.dto.ExecutionResult;
import com.covvee.service.interfaces.ExecutionInterface;
import com.covvee.utils.ProjectMaterializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ExecutionService implements ExecutionInterface {

    private final ProjectMaterializer projectMaterializer;

    @Override
    public ExecutionResult runProject(String projectId) {
        Path tempDir = null;
        try {
            // 1. Create a temp folder
            tempDir = Files.createTempDirectory("covvee_run_" + projectId + "_");

            // 2. MAGIC HAPPENS HERE: Database -> Real Disk
            projectMaterializer.materializeProject(projectId, tempDir);

            // 3. Now 'tempDir' is a perfect copy of the project.
            // You can mount it to Docker!
            // return runDockerContainer(tempDir, ...);

        } catch (IOException e) {
            throw new RuntimeException("Failed to prepare workspace", e);
        } finally {
            // 4. CLEANUP (Crucial!)
            // deleteDirectory(tempDir);
        }
        return null;
    }


    @Override
    public void stopExecution(String executionId) {

    }

    @Override
    public boolean isEnvironmentReady() {
        return false;
    }
}
