package com.covvee.controller;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;
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
    @PreAuthorize("@projectFileSecurity.ownFile(#id,principal)")
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
    @PreAuthorize("@projectFileSecurity.ownFile(#id,principal)")
    public void renameFile(
            @DestinationVariable String id,
            @Valid @Payload RenameFileDto content,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        File file = fileService.renameFile(id, content);
        messagingTemplate.convertAndSend("/topic/project/"+ file.getProjectId(),"update file list");
    }

    @MessageMapping("/delete/{id}")
    @SendTo("/topic/delete/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id,principal)")
    public void deleteFile(
            @DestinationVariable String id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        String projectId = fileService.deleteFile(id);
        messagingTemplate.convertAndSend("/topic/project/"+ projectId,"update file list");
    }

    @MessageMapping("/move/{id}")
    @SendTo("/topic/move/{id}")
    @PreAuthorize("@projectFileSecurity.ownFile(#id, principal)")
    public FileResponse moveFile(
            @DestinationVariable String id,
            @Payload String newParentFolderId,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return fileService.moveFile(id, newParentFolderId);
    }
    @MessageMapping("create/{projectId}")
    @SendTo("/topic/create/{projectId}")
    public FileResponse createFile(
            @DestinationVariable String projectId,
            @Valid @Payload CreateFileRequest request) {
        FileResponse fileResponse = fileService.createFile(request);
        messagingTemplate.convertAndSend("/topic/project/"+ projectId,"update file list");
        return fileResponse;
    }

    @MessageMapping("/project/files/{id}")
    @SendTo("/topic/project/files/{id}")
    public List<FileResponse> files(@DestinationVariable String id) {
        return fileService.getAllFilesByProjectId(id);
    }
}