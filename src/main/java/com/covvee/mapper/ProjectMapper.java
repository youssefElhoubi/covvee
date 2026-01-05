package com.covvee.mapper;



import com.covvee.dto.project.request.CreateProjectRequest;
import com.covvee.dto.project.response.ProjectDetailResponse;
import com.covvee.dto.project.response.ProjectSummaryResponse;
import com.covvee.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class, FolderMapper.class, FileMapper.class})
public interface ProjectMapper {

    // 1. Dashboard View (Summary)
    @Mapping(target = "owner", source = "user") // Entity has 'user', DTO has 'owner'
    @Mapping(target = "fileCount", source = ".", qualifiedByName = "calculateFileCount")
    ProjectSummaryResponse toSummary(Project project);

    // 2. IDE View (Detail)
    // Maps 'folders' (Entity) -> 'rootFolders' (DTO)
    @Mapping(target = "rootFolders", source = "folders")
    // Maps 'rootFiles' (Entity) -> 'rootFiles' (DTO) - Automatic match
    ProjectDetailResponse toDetail(Project project);

    // 3. Creation Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Typo in your Entity 'updatedAT'
    @Mapping(target = "user", ignore = true) // Set in Service
    @Mapping(target = "folders", ignore = true)
    @Mapping(target = "rootFiles", ignore = true)
    Project toEntity(CreateProjectRequest request);

    // Helper to calculate file count for the summary
    // Note: This only counts loaded files. If using DBRef with Lazy Loading,
    // this might be performance heavy or return 0.
    @Named("calculateFileCount")
    default int calculateFileCount(Project project) {
        if (project == null) return 0;

        int count = 0;
        if (project.getRootFiles() != null) {
            count += project.getRootFiles().size();
        }
        // NOTE: A true recursive count here is expensive.
        // For MVP, we often just count the root items or return 0
        // and calculate properly via a DB Aggregation query instead.
        return count;
    }
}