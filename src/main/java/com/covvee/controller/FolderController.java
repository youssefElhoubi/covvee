package com.covvee.controller;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.FolderService;
import com.covvee.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController // changed from @Controller to indicate REST API
@RequiredArgsConstructor
@RequestMapping("/folders") // Changed to standard lowercase plural
public class FolderController { // Renamed class

    private final FolderService folderService;
    private final ProjectService projectService;

    private final SimpMessagingTemplate messagingTemplate;


    // Create a new folder
    // POST /folders
    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(@Valid @RequestBody CreateFolderRequest request) {
        FolderResponse folderResponse = folderService.createFolder(request);
        String projectId = request.getProjectId();
        messagingTemplate.convertAndSend("/topic/project/"+projectId, projectService.getProject(projectId));
        return ResponseEntity.ok(folderResponse);
    }

    // Get a specific folder by ID
    // GET /folders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<FolderResponse> getFolderById(@PathVariable String id) {
        return ResponseEntity.ok(folderService.getFolderById(id));
    }

    // Rename a folder
    // PUT /folders/{id}/rename
    // Note: Accepts the new name as the Request Body
    @PutMapping("/{id}/rename")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,principal)")
    public ResponseEntity<FolderResponse> renameFolder(@PathVariable String id, @RequestBody String name, @AuthenticationPrincipal AppUserDetails userDetails) {

        return ResponseEntity.ok(folderService.renameFolder(id, name));
    }

    // Move a folder
    // PUT /folders/{id}/move
    @PutMapping("/{id}/move")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,principal)")
    public ResponseEntity<FolderResponse> moveFolder(@PathVariable String id, @RequestBody String destination, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(folderService.moveFolder(id, destination));
    }

    // Delete a folder
    // DELETE /folders/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,principal)")
    public ResponseEntity<String> deleteFolder(@PathVariable String id, @AuthenticationPrincipal AppUserDetails userDetails) {
        String projectId = folderService.deleteFolder(id);
        messagingTemplate.convertAndSend("/topic/project/" + projectId, projectService.getProject(projectId));

        // 3. Return success to the frontend
        return ResponseEntity.ok(projectId);
    }
}