package com.covvee.service.interfaces;

import com.covvee.dto.ExecutionResult;

public interface ExecutionInterface {
    ExecutionResult runProject(String projectId);
    void stopExecution(String executionId);
    boolean isEnvironmentReady();
}
