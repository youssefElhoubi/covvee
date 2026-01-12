package com.covvee.dto.file.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RenameFileDto {
    @NotBlank(message = "New name is required")
    @Size(max = 50, message = "File name cannot exceed 50 characters")
    @Pattern(regexp = "^[^\\\\/]*$", message = "File name cannot contain slashes")
    @Pattern(regexp = "^.*\\.[a-zA-Z0-9]+$", message = "File name must have a valid extension")
    private String newName;
}
