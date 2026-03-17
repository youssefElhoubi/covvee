package com.covvee.controller;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.security.AppUserDetails;
import com.covvee.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FolderWebSocket {

    private final FolderService folderService;

    @MessageMapping("/folders.create")
    @SendTo("/topic/folders/new")
    public FolderResponse createFolder(@Valid @Payload CreateFolderRequest request) {
        return folderService.createFolder(request);
    }


    @MessageMapping("/folders.get.{id}")
    @SendTo("/topic/folders/{id}")
    public FolderResponse getFolderById(@DestinationVariable String id) {
        return folderService.getFolderById(id);
    }

    @MessageMapping("/folders.rename.{id}")
    @SendTo("/topic/folders/{id}")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,#userDetails)")
    public FolderResponse renameFolder(
            @DestinationVariable String id,
            @Payload String name,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return folderService.renameFolder(id, name);
    }

    @MessageMapping("/folders.move.{id}")
    @SendTo("/topic/folders/{id}")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,#userDetails)")
    public FolderResponse moveFolder(
            @DestinationVariable String id,
            @Payload String destination,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return folderService.moveFolder(id, destination);
    }

    @MessageMapping("/folders.delete.{id}")
    @SendTo("/topic/folders/deleted")
    @PreAuthorize("@projectFileSecurity.ownFolder(#id,#userDetails)")
    public String deleteFolder(
            @DestinationVariable String id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        folderService.deleteFolder(id);
        return id;
    }
}