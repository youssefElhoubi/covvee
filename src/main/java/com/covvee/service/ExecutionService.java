package com.covvee.service;

import com.covvee.dto.ExecutionResult;
import com.covvee.service.interfaces.ExecutionInterface;
import com.covvee.utils.ProjectMaterializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionService implements ExecutionInterface {

    private final ProjectMaterializer projectMaterializer;

    @Override
    public ExecutionResult runProject(String projectId) {
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
