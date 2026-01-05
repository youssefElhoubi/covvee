package com.covvee.service.interfaces;

import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.request.UpdateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.entity.User;

public interface ProjectServiceInterface {
    ProjectSummaryResponse createProject(CreateProjectRequest dto, User user);
    ProjectSummaryResponse updateProject(UpdateProjectRequest dto,String id);
    void deleteProject(String id);
    ProjectDetailResponse getProject(String id);

}
