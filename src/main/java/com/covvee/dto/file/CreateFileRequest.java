package com.covvee.dto.file;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateFileRequest {

    @NotBlank(message = "File name is required")
    @Size(max = 50, message = "File name cannot exceed 50 characters")
    @Pattern(regexp = "^[^\\\\/]*$", message = "File name cannot contain slashes")
    // Ensures file has an extension (e.g., .java, .py)
    @Pattern(regexp = "^.*\\.[a-zA-Z0-9]+$", message = "File name must have a valid extension (e.g., main.java)")
    private String name;

    @NotBlank(message = "Project ID is required")
    private String projectId;

    private String parentFolderId; // Optional

    // Jakarta Validation checks character count, not byte size.
    // 100,000 chars is roughly 100KB-200KB.
    @Size(max = 100000, message = "File content is too large (max 100,000 characters)")
    private String content;
}