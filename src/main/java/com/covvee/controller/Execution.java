package com.covvee.controller;

import com.covvee.dto.ExecutionResult;
import com.covvee.service.DockerService;
import com.covvee.service.ExecutionService;
import com.covvee.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("execute")
public class Execution {
    private final ExecutionService executionService;
    @GetMapping("project")
    public ResponseEntity<ExecutionResult> execute(@RequestBody String projectId) {
        return ResponseEntity.ok(executionService.runProject(projectId));
    }
}
