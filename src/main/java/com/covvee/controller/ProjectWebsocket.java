package com.covvee.controller;

import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ProjectWebsocket {
    private final ProjectService projectService;
    @MessageMapping("/project{id}")
    @SendTo("/topic/project/{id}")
    public ProjectDetailResponse getProject(
            @DestinationVariable String id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return projectService.getProject(id);
    }
}
