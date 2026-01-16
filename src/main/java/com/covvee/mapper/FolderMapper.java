package com.covvee.mapper;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.entity.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface FolderMapper {

    // 1. Entity -> Response (Recursive)
    // MapStruct automatically detects:
    // - List<Folder> children -> invokes toResponse() recursively
    // - List<File> files     -> invokes FileMapper.toResponse()
    FolderResponse toResponse(Folder folder);

    // 2. Request -> Entity (Creation)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())") // Initialize empty list
    @Mapping(target = "files", expression = "java(new java.util.ArrayList<>())")    // Initialize empty list
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    // IMPORTANT: Map the DTO's "parentFolderId" to the Entity's "parentId"
    @Mapping(source = "parentFolderId", target = "parentId")
    Folder toEntity(CreateFolderRequest request);
}