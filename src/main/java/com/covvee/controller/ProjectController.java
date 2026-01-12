package com.covvee.controller;

import com.covvee.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("project")
public class ProjectController {
    private final ProjectService projectService;

}
