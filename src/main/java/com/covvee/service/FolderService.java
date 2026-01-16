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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService implements FolderInterface {
    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;
    @Override
    public FolderResponse createFolder(CreateFolderRequest request) {
        Folder folder = folderMapper.toEntity(request);
        folder = folderRepository.save(folder);
        if (request.getParentFolderId() != null) {
            Folder parent = folderRepository.findById(folder.getParentId()).orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
            parent.getChildren().add(folder);
            folderRepository.save(parent);
        }
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
        // 1. Fetch the folder we want to move
        Folder folderToMove = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));

        // 2. Fetch the destination parent
        Folder newParent = folderRepository.findById(newParentId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination folder not found"));

        // 3. VALIDATION: Check for Cycles
        // We need to verify that 'newParentId' is NOT a descendant of 'folderId'

        // OPTIMIZATION: Instead of querying the DB in a loop, fetch all folders for this project
        // This is fast because projects rarely have thousands of folders.
        List<Folder> allFolders = folderRepository.findByProjectId(folderToMove.getProjectId());

        // Create a fast lookup map: ID -> ParentID
        Map<String, String> parentMap = allFolders.stream()
                .collect(Collectors.toMap(Folder::getId, f -> f.getParentId() != null ? f.getParentId() : "ROOT"));

        // Walk up from the destination
        String currentId = newParentId;
        while (currentId != null && !currentId.equals("ROOT")) {
            // FAIL CONDITION: We bumped into the folder we are trying to move!
            if (currentId.equals(folderId)) {
                throw new IllegalArgumentException("Cannot move a folder inside its own sub-folder.");
            }
            // Move one step up the tree
            currentId = parentMap.get(currentId);
        }

        // 4. If we pass the check, perform the move
        // Note: You must also handle removing it from the old parent's 'children' list
        // if you are maintaining that list manually.

        folderToMove.setParentId(newParentId);
        Folder savedFolder = folderRepository.save(folderToMove);

        return folderMapper.toResponse(savedFolder);
    }

    @Override
    public void deleteFolder(String folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        if (folder.getParentId() == null) {
            folderRepository.delete(folder);
            return;
        }

    }

    @Override
    public List<FolderResponse> getAllFoldersByProjectId(String projectId) {
        return List.of();
    }
}
