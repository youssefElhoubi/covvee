package com.covvee.dto.response;

import com.covvee.dto.file.response.FileResponse;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.enums.Language;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProjectDetailResponse {
    private String id;
    private String name;
    private Language language;

    // The File System Tree
    // These are the files/folders sitting at the root (top level)
    private List<FileResponse> rootFiles;
    private List<FolderResponse> rootFolders;
}