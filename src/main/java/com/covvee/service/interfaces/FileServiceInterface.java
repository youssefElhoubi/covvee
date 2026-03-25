package com.covvee.service.interfaces;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.DeleteFileDto;
import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;

import java.util.List;

public interface FileServiceInterface {

    FileResponse createFile(CreateFileRequest request);

    FileResponse getFileById(String fileId);

    FileResponse updateFileContent(String fileId, UpdateFileDto content);

    File renameFile(String fileId, RenameFileDto newName);

    String deleteFile(DeleteFileDto fileId);

    FileResponse moveFile(String fileId, String newParentFolderId);

    List<FileResponse> getAllFilesByProjectId(String projectId);
}
