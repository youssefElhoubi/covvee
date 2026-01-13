package com.covvee.service.interfaces;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;

import java.util.List;

public interface FileServiceInterface {

    FileResponse createFile(CreateFileRequest request);

    FileResponse getFileById(String fileId);

    FileResponse updateFileContent(String fileId, UpdateFileDto content);

    FileResponse renameFile(String fileId, RenameFileDto newName);

    void deleteFile(String fileId);

    FileResponse moveFile(String fileId, String newParentFolderId);

    List<FileResponse> getAllFilesByProjectId(String projectId);
}
