package com.covvee.controller;

import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@MessageMapping("file")
public class FileWebSocket {

    private final FileService fileService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/update/{id}")
    @SendTo("/topic/data/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public FileResponse updateContent(
            @DestinationVariable String id,
            @Valid @Payload UpdateFileDto content,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return fileService.updateFileContent(id, content);
    }

    @MessageMapping("/request/{id}")
    @SendTo("/topic/file/{id}")
    public FileResponse requestFile(@DestinationVariable String id) {
        return fileService.getFileById(id);
    }

    @MessageMapping("/rename/{id}")
    @SendTo("/topic/rename/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public FileResponse renameFile(
            @DestinationVariable String id,
            @Valid @Payload RenameFileDto content,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return fileService.renameFile(id, content);
    }

    @MessageMapping("/delete/{id}")
    @SendTo("/topic/delete/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public String deleteFile(
            @DestinationVariable String id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        fileService.deleteFile(id);
        return id; // Returned the ID so your React frontend knows which file to remove from the UI
    }

    @MessageMapping("/move/{id}")
    @SendTo("/topic/move/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,userDetails)")
    public FileResponse moveFile(
            @DestinationVariable String id,
            @Payload String newParentFolderId,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return fileService.moveFile(id, newParentFolderId);
    }

    @MessageMapping("/project/files/{id}")
    @SendTo("/topic/project/files/{id}")
    public List<FileResponse> files(@DestinationVariable String id) {
        return fileService.getAllFilesByProjectId(id);
    }
}