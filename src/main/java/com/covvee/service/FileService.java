package com.covvee.service;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;
import com.covvee.mapper.FileMapper;
import com.covvee.repository.FileRepository;
import com.covvee.service.interfaces.FileServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService implements FileServiceInterface {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

//    rest
    @Override
    public FileResponse createFile(CreateFileRequest request) {
        File file = fileMapper.toEntity(request) ;
        return fileMapper.toResponse(fileRepository.save(file));
    }

//    socket
    @Override
    public FileResponse getFileById(String fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()-> new ResourceAccessException("File not found"));
        return fileMapper.toResponse(file);
    }
//    socket
    @Override
    public FileResponse updateFileContent(String fileId, UpdateFileDto content) {
        File file = fileRepository.findById(fileId).orElseThrow(()-> new ResourceAccessException("File not found"));
        byte[] decodeBytes = Base64.getDecoder().decode(content.getContent());
        String decodeContent = new String(decodeBytes, StandardCharsets.UTF_8);
        file.setContent(decodeContent);
        return fileMapper.toResponse(fileRepository.save(file));
    }
//    socket
    @Override
    public FileResponse renameFile(String fileId, RenameFileDto newName) {
        File file = fileRepository.findById(fileId).orElseThrow(()-> new ResourceAccessException("File not found"));
        file.setName(newName.getNewName());
        return fileMapper.toResponse(fileRepository.save(file));
    }
//    rest
    @Override
    public void deleteFile(String fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()-> new ResourceAccessException("File not found"));
        fileRepository.delete(file);
    }
//    rest
    @Override
    public FileResponse moveFile(String fileId, String newParentFolderId) {
        File file = fileRepository.findById(fileId).orElseThrow(()-> new ResourceAccessException("File not found"));
        file.setParentId(newParentFolderId);
        return fileMapper.toResponse(fileRepository.save(file));
    }
//    socket
    @Override
    public List<FileResponse> getAllFilesByProjectId(String projectId) {
        List<File> files = fileRepository.findByProjectId(projectId);
        return files.stream().map(fileMapper::toResponse).toList();
    }
}
