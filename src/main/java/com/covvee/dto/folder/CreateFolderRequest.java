package com.covvee.dto.folder;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateFolderRequest {

    @NotBlank(message = "Folder name is required")
    @Size(max = 50, message = "Folder name cannot exceed 50 characters")
    // Blocks ".." or "/" to prevent directory traversal attacks
    @Pattern(regexp = "^[^\\\\/]*$", message = "Folder name cannot contain slashes")
    private String name;

    @NotBlank(message = "Project ID is required")
    private String projectId;

    // Optional: Can be null if creating a root folder
    private String parentFolderId;
}