package com.covvee.service.interfaces;

import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;

public interface ProjectServiceInterface {
    ProjectSummaryResponse createProject(CreateProjectRequest dto);
    ProjectSummaryResponse updateProject(CreateProjectRequest dto);
    void deleteProject(String id);
    ProjectDetailResponse getProject(String id);

}
