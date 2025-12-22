package com.covvee.dto.project.response;

import com.covvee.dto.auth.response.UserResponse;
import com.covvee.enums.Language;
import com.covvee.enums.Visibility;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProjectSummaryResponse {
    private String id;
    private String name;
    private String description;
    private Language language;
    private Visibility visibility;
    private UserResponse owner;     // Brief info about the creator
    private LocalDateTime updatedAt;
    private int fileCount;          // Useful for UI stats
}