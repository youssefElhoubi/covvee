package com.covvee.service;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.DeleteFileDto;
import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;
import com.covvee.entity.Folder;
import com.covvee.entity.Project;
import com.covvee.mapper.FileMapper;
import com.covvee.repository.FileRepository;
import com.covvee.repository.FolderRepository;
import com.covvee.repository.ProjectRepository;
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
    private final FolderRepository folderRepository;
    private final ProjectRepository projectRepository;


    @Override
    public FileResponse createFile(CreateFileRequest request) {
        File file = fileMapper.toEntity(request);
        file = fileRepository.save(file);
        if (request.getParentFolderId() == null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceAccessException("Project not found"));
            project.getRootFiles().add(file);
            projectRepository.save(project);
        } else {
            Folder parentFolder = folderRepository.findById(request.getParentFolderId())
                    .orElseThrow(() -> new ResourceAccessException("Folder not found"));
            parentFolder.getFiles().add(file);
            folderRepository.save(parentFolder);
        }
        return fileMapper.toResponse(file);
    }

    //    socket
    @Override
    public FileResponse getFileById(String fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ResourceAccessException("File not found"));
        return fileMapper.toResponse(file);
    }

    //    socket
    @Override
    public FileResponse updateFileContent(String fileId, UpdateFileDto content) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceAccessException("File not found"));
        file.setContent(content.getContent());

        return fileMapper.toResponse(fileRepository.save(file));
    }

    //    socket
    @Override
    public File renameFile(String fileId, RenameFileDto newName) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ResourceAccessException("File not found"));
        file.setName(newName.getNewName());
        return fileRepository.save(file);
    }

    //    rest
    @Override
    public String deleteFile(DeleteFileDto request) {
        File file = fileRepository.findById(request.getFileId())
                .orElseThrow(() -> new ResourceAccessException("File not found"));
        String projectID = file.getProjectId();
        if (file.getParentId() != null) {
            Folder parentFolder = folderRepository.findById(file.getParentId())
                    .orElseThrow(() -> new ResourceAccessException("Folder not found"));
            parentFolder.getFiles().removeIf(f -> f.getId().equals(file.getId()));
            folderRepository.save(parentFolder);
        } else {
            Project project = projectRepository.findById(projectID)
                    .orElseThrow(() -> new ResourceAccessException("Project not found"));
            project.getRootFiles().removeIf(f -> f.getId().equals(file.getId()));
            projectRepository.save(project);
        }
        fileRepository.delete(file);
        return projectID;
    }

    //    rest
    @Override
    public FileResponse moveFile(String fileId, String newParentFolderId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ResourceAccessException("File not found"));
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
