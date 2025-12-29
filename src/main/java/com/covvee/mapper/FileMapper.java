package com.covvee.mapper;

import com.covvee.dto.file.request.CreateFileRequest;
import com.covvee.dto.file.response.FileResponse;
import com.covvee.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    // Entity -> Response
    // Using "parentId" depends on your DB strategy.
    // If you store parentId in the entity, map it here.
    FileResponse toResponse(File file);

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    File toEntity(CreateFileRequest request);
}