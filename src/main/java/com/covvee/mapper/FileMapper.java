package com.covvee.mapper;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.request.RenameFileDto;
import com.covvee.dto.file.request.UpdateFileDto;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FileMapper {
    @Mapping(target = "parentId", source = "parentFolderId")
    @Mapping(target = "content", constant = "")
    File toEntity(CreateFileRequest request);

    void updateFileFromDto(UpdateFileDto dto, @MappingTarget File entity);

    @Mapping(target = "name", source = "newName")
    void renameFileFromDto(RenameFileDto dto, @MappingTarget File entity);
    FileResponse toResponse(File file);

}