package com.covvee.dto.user.response;

import com.covvee.dto.project.response.ProjectSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String role;
    private boolean isBanned;
    private LocalDateTime createdAt;

    private int projectCount; // Total number of projects
    private List<ProjectSummaryResponse> projects; // The list of projects for the UI table

}
