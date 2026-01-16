package com.covvee.service;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.entity.Folder;
import com.covvee.execption.ResourceNotFoundException;
import com.covvee.mapper.FolderMapper;
import com.covvee.repository.FolderRepository;
import com.covvee.service.interfaces.folder.FolderInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService implements FolderInterface {
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    @Override
    public FolderResponse createFolder(CreateFolderRequest request) {
        Folder folder = folderMapper.toEntity(request);
        folder = folderRepository.save(folder);
        return folderMapper.toResponse(folder);
    }

    @Override
    public FolderResponse getFolderById(String folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        return folderMapper.toResponse(folder);
    }

    @Override
    public FolderResponse renameFolder(String folderId, String newName) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        folder.setName(newName);
        folder = folderRepository.save(folder);
        return folderMapper.toResponse(folder);
    }

    @Override
    public FolderResponse moveFolder(String folderId, String newParentId) {
        Folder folderToMove = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        // 2. Fetch the destination parent
        Folder newParent = folderRepository.findById(newParentId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination folder not found"));
//        check if the new parent folder was a child folder then move
        return null;
    }

    @Override
    public void deleteFolder(String folderId) {

    }

    @Override
    public List<FolderResponse> getAllFoldersByProjectId(String projectId) {
        return List.of();
    }
}
