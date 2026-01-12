package com.covvee.dto.file.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateFileRequest {

    @NotBlank(message = "File name is required")
    @Size(max = 50, message = "File name cannot exceed 50 characters")
    @Pattern(regexp = "^[^\\\\/]*$", message = "File name cannot contain slashes")
    @Pattern(regexp = "^.*\\.[a-zA-Z0-9]+$", message = "File name must have a valid extension (e.g., main.java)")
    private String name;
    @NotBlank(message = "Project ID is required")
    private String projectId;
    private String parentFolderId;
}