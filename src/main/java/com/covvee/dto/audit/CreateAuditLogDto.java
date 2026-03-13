package com.covvee.dto.audit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuditLogDto {

    @NotBlank(message = "Admin ID cannot be empty")
    private String adminId;

    @NotBlank(message = "Admin username cannot be empty")
    private String adminUsername;

    @NotBlank(message = "Action type is required")
    @Size(max = 50, message = "Action identifier cannot exceed 50 characters")
    private String action;        // e.g., "BANNED_USER", "DELETED_PROJECT"

    @NotBlank(message = "Target ID is required to know what was affected")
    private String targetId;      // e.g., The ID of the banned user or deleted project

    @Size(max = 255, message = "Details cannot exceed 255 characters")
    private String details;       // e.g., "Violation of TOS"
}