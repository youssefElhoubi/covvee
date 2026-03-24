package com.covvee.service.interfaces;

import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.request.UpdateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.entity.Project;
import com.covvee.entity.User;
import com.covvee.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectServiceInterface {
    ProjectSummaryResponse createProject(CreateProjectRequest dto, User user);
    ProjectSummaryResponse updateProject(UpdateProjectRequest dto,String id);
    void deleteProject(String id);
    ProjectDetailResponse getProject(String id);
    List<ProjectDetailResponse> allProjects();
    List<ProjectDetailResponse> userProjects(User user);
    Page<Project> ProjectSearch(Language language, String name, int page, int size);


}
