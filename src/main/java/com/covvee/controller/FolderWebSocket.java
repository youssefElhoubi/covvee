package com.covvee.controller;

import com.covvee.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("Folder")
public class FolderWebSocket {
    private final FolderService folderService;
}
