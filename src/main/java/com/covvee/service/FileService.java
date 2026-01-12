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

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService implements FileServiceInterface {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Override
    public FileResponse createFile(CreateFileRequest request) {
        File file = fileMapper.toEntity(request) ;
        return fileMapper.toResponse(fileRepository.save(file));
    }

    @Override
    public FileResponse getFileById(String fileId) {
        return null;
    }

    @Override
    public FileResponse updateFileContent(String fileId, UpdateFileDto content) {
        return null;
    }

    @Override
    public FileResponse renameFile(String fileId, RenameFileDto newName) {
        return null;
    }

    @Override
    public void deleteFile(String fileId) {

    }

    @Override
    public FileResponse moveFile(String fileId, String newParentFolderId) {
        return null;
    }

    @Override
    public List<FileResponse> getAllFilesByProjectId(String projectId) {
        return List.of();
    }
}
