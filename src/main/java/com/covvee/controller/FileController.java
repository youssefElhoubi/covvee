package com.covvee.controller;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {
    private final FileService fileService;

    @PostMapping("create")
    public ResponseEntity<FileResponse> createFile(@RequestBody CreateFileRequest createFileRequest) {
        FileResponse fileResponse = fileService.createFile(createFileRequest);
        return ResponseEntity.ok(fileResponse);
    }

}
