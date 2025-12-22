package com.covvee.dto.folder.response;

import com.covvee.dto.file.response.FileResponse;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class FolderResponse {
    private String id;
    private String name;      // e.g., "src"
    private String parentId;  // ID of the parent folder

    // RECURSION HAPPENS HERE
    private List<FolderResponse> children; // Sub-folders inside this folder
    private List<FileResponse> files;      // Files inside this folder
}