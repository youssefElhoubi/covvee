package com.covvee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutionResult {
    private String output;
    private String error;
    private int exitCode;
    private long executionTimeMs;
    private boolean isTimeout;
}
