package com.covvee.controller;

import com.covvee.dto.ExecutionResult;
import com.covvee.service.DockerService;
import com.covvee.service.ExecutionService;
import com.covvee.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/execute")
public class Execution {
    private final ExecutionService executionService;
    @PostMapping("/project")
    public ResponseEntity<ExecutionResult> execute(@RequestBody String projectId) {
        return ResponseEntity.ok(executionService.runProject(projectId));
    }
}
