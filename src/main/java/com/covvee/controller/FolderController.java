package com.covvee.controller;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // changed from @Controller to indicate REST API
@RequiredArgsConstructor
@RequestMapping("/folders") // Changed to standard lowercase plural
public class FolderController { // Renamed class

    private final FolderService folderService;

    // Create a new folder
    // POST /folders
    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(@RequestBody CreateFolderRequest request) {
        return ResponseEntity.ok(folderService.createFolder(request));
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
    public ResponseEntity<FolderResponse> renameFolder(@PathVariable String id, @RequestBody String name) {
        return ResponseEntity.ok(folderService.renameFolder(id, name));
    }

    // Move a folder
    // PUT /folders/{id}/move
    @PutMapping("/{id}/move")
    public ResponseEntity<FolderResponse> moveFolder(@PathVariable String id, @RequestBody String destination) {
        return ResponseEntity.ok(folderService.moveFolder(id, destination));
    }

    // Delete a folder
    // DELETE /folders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFolder(@PathVariable String id) {
        folderService.deleteFolder(id);
        return ResponseEntity.ok("Folder deleted");
    }
}