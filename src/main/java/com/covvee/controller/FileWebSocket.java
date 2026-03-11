package com.covvee.controller;

import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("file")
public class FileWebSocket {
    private final FileService fileService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping()
    @SendTo("topic/data/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public ResponseEntity<FileResponse> updateContent(@PathVariable String id, @Valid @RequestBody UpdateFileDto content, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(fileService.updateFileContent(id, content));
    }

    @MessageMapping()
    @SendTo("topic/file/{id}")
    public ResponseEntity<FileResponse> requestFile(@PathVariable String id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @MessageMapping()
    @SendTo("topic/rename/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public ResponseEntity<FileResponse> renameFile(@PathVariable String id,@Valid @RequestBody RenameFileDto content, @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(fileService.renameFile(id, content));
    }

    @MessageMapping()
    @SendTo("topic/delete/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public ResponseEntity<?> deleteFile(@PathVariable String id, @AuthenticationPrincipal AppUserDetails userDetails) {
        fileService.deleteFile(id);
        return ResponseEntity.ok().build();
    }

    @MessageMapping()
    @SendTo("topic/move/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public ResponseEntity<FileResponse> moveFile(@PathVariable String id, @RequestBody String newParentFolderId , @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(fileService.moveFile(id, newParentFolderId));
    }

    @MessageMapping()
    @SendTo("topic/project/files/{id}")
    public ResponseEntity<List<FileResponse>> files(@PathVariable String id) {
        return ResponseEntity.ok(fileService.getAllFilesByProjectId(id));
    }


}
