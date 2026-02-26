package com.covvee.service;

import com.covvee.dto.ExecutionResult;
import com.covvee.entity.Project;
import com.covvee.repository.ProjectRepository;
import com.covvee.service.interfaces.ExecutionInterface;
import com.covvee.utils.ProjectMaterializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public ExecutionResult runProject(String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceAccessException("project was not found "));
        Path tempDir = null;
        try {
            // 1. Create a temp folder
            tempDir = Files.createTempDirectory("covvee_run_" + projectId + "_");

            // 2. MAGIC HAPPENS HERE: Database -> Real Disk
            projectMaterializer.materializeProject(projectId, tempDir);

            // 3. Now 'tempDir' is a perfect copy of the project.
            // You can mount it to Docker!
            dockerService.execute(tempDir, project.getLanguage());
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
