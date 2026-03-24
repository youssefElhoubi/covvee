package com.covvee.controller;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.DeleteFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.service.FileService;
import com.covvee.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {

    private final FileService fileService;
    private final ProjectService projectService;

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("create")
    public ResponseEntity<FileResponse> createFile(@Valid @RequestBody CreateFileRequest createFileRequest) {
        FileResponse fileResponse = fileService.createFile(createFileRequest);

        String projectId = createFileRequest.getProjectId();
        projectService.getProject(projectId);

        // 4. Broadcast to the topic
        messagingTemplate.convertAndSend("/topic/project/" + projectId, projectService.getProject(projectId));

        return ResponseEntity.ok(fileResponse);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteFile(@Valid @RequestBody DeleteFileDto createFileRequest) {
        String fileResponse = fileService.deleteFile(createFileRequest.getFileId());

        String projectId = createFileRequest.getProjectId();
        projectService.getProject(projectId);

        // 4. Broadcast to the topic
        messagingTemplate.convertAndSend("/topic/project/" + projectId, projectService.getProject(projectId));

        return ResponseEntity.ok(fileResponse);
    }
}