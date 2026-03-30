package com.covvee.service;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.DeleteFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;
import com.covvee.entity.Folder;
import com.covvee.entity.Project;
import com.covvee.execption.ResourceNotFoundException;
import com.covvee.mapper.FileMapper;
import com.covvee.repository.FileRepository;
import com.covvee.repository.FolderRepository;
import com.covvee.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileMapper fileMapper;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private FileService fileService;

    private File mockFile;
    private Project mockProject;
    private Folder mockFolder;
    private FileResponse mockFileResponse;

    @BeforeEach
    void setUp() {
        mockFile = new File();
        mockFile.setId("file-123");
        mockFile.setProjectId("proj-123");

        mockProject = new Project();
        mockProject.setId("proj-123");
        mockProject.setRootFiles(new ArrayList<>());

        mockFolder = new Folder();
        mockFolder.setId("folder-123");
        mockFolder.setFiles(new ArrayList<>());

        mockFileResponse = new FileResponse();
        mockFileResponse.setId("file-123");
    }

    // --- TEST CREATE FILE ---

    @Test
    void createFile_inRootProject_Success() {
        // Arrange
        CreateFileRequest request = new CreateFileRequest();
        request.setProjectId("proj-123");
        request.setParentFolderId(null); // Root file

        when(fileMapper.toEntity(request)).thenReturn(mockFile);
        when(fileRepository.save(mockFile)).thenReturn(mockFile);
        when(projectRepository.findById("proj-123")).thenReturn(Optional.of(mockProject));
        when(fileMapper.toResponse(mockFile)).thenReturn(mockFileResponse);

        // Act
        FileResponse response = fileService.createFile(request);

        // Assert
        assertNotNull(response);
        assertTrue(mockProject.getRootFiles().contains(mockFile));
        verify(projectRepository, times(1)).save(mockProject);
        verify(folderRepository, never()).findById(any());
    }

    @Test
    void createFile_inFolder_Success() {
        // Arrange
        CreateFileRequest request = new CreateFileRequest();
        request.setProjectId("proj-123");
        request.setParentFolderId("folder-123"); // Nested file

        when(fileMapper.toEntity(request)).thenReturn(mockFile);
        when(fileRepository.save(mockFile)).thenReturn(mockFile);
        when(folderRepository.findById("folder-123")).thenReturn(Optional.of(mockFolder));
        when(fileMapper.toResponse(mockFile)).thenReturn(mockFileResponse);

        // Act
        FileResponse response = fileService.createFile(request);

        // Assert
        assertNotNull(response);
        assertTrue(mockFolder.getFiles().contains(mockFile));
        verify(folderRepository, times(1)).save(mockFolder);
        verify(projectRepository, never()).findById(any());
    }

    @Test
    void createFile_ProjectNotFound_ThrowsException() {
        // Arrange
        CreateFileRequest request = new CreateFileRequest();
        request.setProjectId("invalid-proj");
        request.setParentFolderId(null);

        when(fileMapper.toEntity(request)).thenReturn(mockFile);
        when(fileRepository.save(mockFile)).thenReturn(mockFile);
        when(projectRepository.findById("invalid-proj")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> fileService.createFile(request));
    }

    // --- TEST DELETE FILE ---

    @Test
    void deleteFile_fromRootProject_Success() {
        // Arrange
        DeleteFileDto request = new DeleteFileDto();
        request.setFileId("file-123");

        mockFile.setParentId(null); // Root file
        mockProject.getRootFiles().add(mockFile);

        when(fileRepository.findById("file-123")).thenReturn(Optional.of(mockFile));
        when(projectRepository.findById("proj-123")).thenReturn(Optional.of(mockProject));

        // Act
        String projectId = fileService.deleteFile(request);

        // Assert
        assertEquals("proj-123", projectId);
        assertFalse(mockProject.getRootFiles().contains(mockFile));
        verify(projectRepository, times(1)).save(mockProject);
        verify(fileRepository, times(1)).delete(mockFile);
    }
}