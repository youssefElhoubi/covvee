package com.covvee.controller;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("folder")
public class FolderWebSocket {
    private final FolderService folderService;

    @MessageMapping()
    @SendTo("topic/folder")
    public ResponseEntity<FolderResponse> createFolder(@Payload CreateFolderRequest request){
        return ResponseEntity.ok(folderService.createFolder(request));
    }

    @MessageMapping()
    @SendTo("topic/folder/{id}")
    public ResponseEntity<FolderResponse> getFolderById(@PathVariable String id){
        return ResponseEntity.ok(folderService.getFolderById(id));
    }
    @MessageMapping()
    @SendTo("topic/folder/rename/{id}")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,userDetails)")
    public ResponseEntity<FolderResponse> renameFolder(@PathVariable String id, @Payload String name, @AuthenticationPrincipal AppUserDetails userDetails){
        return ResponseEntity.ok(folderService.renameFolder(id, name));
    }
    @MessageMapping()
    @SendTo("topic/folder/move/{id}")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,userDetails)")
    public ResponseEntity<FolderResponse> moveFolder(@PathVariable String id, @Payload String name, @AuthenticationPrincipal AppUserDetails userDetails){
        return ResponseEntity.ok(folderService.moveFolder(id, name));
    }
    @MessageMapping()
    @SendTo("topic/folder/delete/{id}")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,userDetails)")
    public ResponseEntity<String> deleteFolder(@PathVariable String id, @AuthenticationPrincipal AppUserDetails userDetails){
        folderService.deleteFolder(id);
        return ResponseEntity.ok("Folder deleted");
    }
}
