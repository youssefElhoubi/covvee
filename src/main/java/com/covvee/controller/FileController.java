package com.covvee.controller;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.DeleteFileDto;
import com.covvee.dto.file.request.RenameFileDto;
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
    public ResponseEntity<String> deleteFile(@Valid @RequestBody DeleteFileDto deleteRequest) {

        // 1. Pass BOTH IDs to the service to safely clean up MongoDB.
        // The service returns the projectId, so we save it right here!
        String projectId = fileService.deleteFile(deleteRequest);

        // 2. Fetch the updated tree and broadcast it to the WebSocket
        messagingTemplate.convertAndSend("/topic/project/" + projectId, projectService.getProject(projectId));

        // 3. Return success to the frontend
        return ResponseEntity.ok(projectId);
    }
    @PutMapping("rename")
    public ResponseEntity<String> renameFile(@Valid @RequestBody RenameFileDto renameRequest) {

        // 1. Pass the file ID and the new name to the service.
        // The service updates the name, saves it, and returns the projectId.
        fileService.renameFile(renameRequest.getFileId(), renameRequest);
        String projectId = renameRequest.getProjectId();

        // 2. Fetch the newly updated tree and broadcast it to the WebSocket
        messagingTemplate.convertAndSend("/topic/project/" + projectId, projectService.getProject(projectId));

        // 3. Return the projectId to the frontend (just like delete)
        return ResponseEntity.ok(projectId);
    }

}