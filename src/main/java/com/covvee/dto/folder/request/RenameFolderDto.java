package com.covvee.dto.folder.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RenameFolderDto {
    @NotBlank(message = "New name is required")
    @Size(max = 50, message = "File name cannot exceed 50 characters")
    @Pattern(regexp = "^[^\\\\/]*$", message = "File name cannot contain slashes")
    @Pattern(regexp = "^.*\\.[a-zA-Z0-9]+$", message = "File name must have a valid extension")
    private String newName;
    private String folderId;
    private String projectId;
}
