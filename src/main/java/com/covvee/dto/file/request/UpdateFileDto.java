package com.covvee.dto.file.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateFileDto {
    @Size(max = 100000, message = "File content is too large (max 100,000 characters)")
    private String content;
}
