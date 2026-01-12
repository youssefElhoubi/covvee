package com.covvee.controller;

import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.request.UpdateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("project")
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping
    public ResponseEntity<ProjectSummaryResponse> createProject(
            @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        ProjectSummaryResponse response = projectService.createProject(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectSummaryResponse> updateProject(
            @PathVariable String id,
            @RequestBody UpdateProjectRequest request) {

        ProjectSummaryResponse response = projectService.updateProject(request, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponse> getProject(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<ProjectDetailResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.allProjects());
    }

}
