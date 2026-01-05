package com.covvee.service;

import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.entity.Project;
import com.covvee.mapper.ProjectMapper;
import com.covvee.repository.ProjectRepository;
import com.covvee.service.interfaces.ProjectServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService implements ProjectServiceInterface {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectSummaryResponse createProject(CreateProjectRequest dto) {
        Project project = projectMapper.toEntity(dto);
        project =  projectRepository.save(project);
        return projectMapper.toSummary(project);
    }

    @Override
    public ProjectSummaryResponse updateProject(CreateProjectRequest dto) {
        return null;
    }

    @Override
    public void deleteProject(String id) {

    }

    @Override
    public ProjectDetailResponse getProject(String id) {
        return null;
    }
}
