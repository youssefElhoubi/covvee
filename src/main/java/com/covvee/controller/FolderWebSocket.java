package com.covvee.controller;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("Folder")
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
}
