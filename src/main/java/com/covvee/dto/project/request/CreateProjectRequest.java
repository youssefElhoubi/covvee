package com.covvee.dto.project.request;

import com.covvee.enums.Language;
import com.covvee.enums.Visibility;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(min = 3, max = 50, message = "Project name must be between 3 and 50 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Visibility is required (PUBLIC or PRIVATE)")
    private Visibility visibility;

    @NotNull(message = "Language is required (PYTHON, JAVA, JS)")
    private Language language;
}