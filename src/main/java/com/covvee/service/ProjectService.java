package com.covvee.service;

import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.request.UpdateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.entity.Project;
import com.covvee.entity.User;
import com.covvee.mapper.ProjectMapper;
import com.covvee.repository.ProjectRepository;
import com.covvee.service.interfaces.ProjectServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
public class ProjectService implements ProjectServiceInterface {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectSummaryResponse createProject(CreateProjectRequest dto, User user) {
        Project project = projectMapper.toEntity(dto);
        project =  projectRepository.save(project);
        project.setUser(user);
        return projectMapper.toSummary(project);
    }

    @Override
    public ProjectSummaryResponse updateProject(UpdateProjectRequest dto,String id) {
        Project project = projectRepository.findById(id).orElseThrow(()-> {
            throw new ResourceAccessException("Project not found");
        });

        project.setDescription(dto.getDescription());
        project.setVisibility(dto.getVisibility());
        project.setName(dto.getName());

        projectRepository.save(project);
        return null;
    }

    @Override
    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectDetailResponse getProject(String id) {
        Project project = projectRepository.findById(id).orElseThrow(()-> {
            throw new ResourceAccessException("Project not found");
        });
        return projectMapper.toDetail(project);
    }
}
