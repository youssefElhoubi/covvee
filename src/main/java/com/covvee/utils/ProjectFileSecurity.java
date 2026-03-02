package com.covvee.utils;

import com.covvee.entity.File;
import com.covvee.entity.User;
import com.covvee.execption.ResourceNotFoundException;
import com.covvee.repository.FileRepository;
import com.covvee.repository.ProjectRepository;
import com.covvee.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("projectFileSecurity")
public class ProjectFileSecurity {
    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;

    public boolean ownProject(String projectId, AppUserDetails authentication) {
        User user = authentication.getUser();
        return projectRepository.findByIdAndUser(projectId, user);
    }
    public boolean ownFile(String fileId, AppUserDetails authentication) {
        User user = authentication.getUser();
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("file not found"));

        return projectRepository.findByIdAndUser(file.getProjectId(), user);
    }
}
