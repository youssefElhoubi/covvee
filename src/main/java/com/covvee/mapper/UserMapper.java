package com.covvee.mapper;

import com.covvee.dto.auth.request.RegisterRequest;
import com.covvee.dto.auth.response.AuthResponse;
import com.covvee.dto.auth.response.UserResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.dto.user.response.UserResponseDTo;
import com.covvee.entity.Project;
import com.covvee.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity -> Response (Hide password)
    @Mapping(target = "joinedAt", source = "createdAt")
    UserResponse toResponse(User user);
    @Mapping(target="token" , ignore = true)
    @Mapping(target="refreshToken" , ignore = true)
    AuthResponse  toAuthResponse(User user);

    // Request -> Entity (Password encoding happens in Service, not here)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER") // Default role
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "projectCount", ignore = true)
    @Mapping(target = "projects", ignore = true)
    UserResponseDTo toBaseUserResponse(User user);

    // 2. Map the individual project to the summary
    ProjectSummaryResponse toProjectSummary(Project project);

    // 3. The Bulletproof Custom Method to combine them
    default UserResponseDTo toUserResponse(User user, List<Project> projects) {
        if (user == null) {
            return null;
        }

        // Map the base user fields (id, username, email, etc.)
        UserResponseDTo response = toBaseUserResponse(user);

        // Safely handle the projects list and count
        if (projects != null && !projects.isEmpty()) {
            response.setProjectCount(projects.size());

            List<ProjectSummaryResponse> projectSummaries = projects.stream()
                    .map(this::toProjectSummary)
                    .toList();

            response.setProjects(projectSummaries);
        } else {
            response.setProjectCount(0);
            response.setProjects(List.of()); // Returns an empty list instead of null
        }

        return response;
    }
}