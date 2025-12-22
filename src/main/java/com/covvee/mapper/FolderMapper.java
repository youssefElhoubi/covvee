package com.covvee.mapper;


import com.covvee.dto.folder.response.FolderResponse;
import com.covvee.entity.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface FolderMapper {

    // MapStruct automatically maps:
    // List<Folder> children -> List<FolderResponse> children
    // List<File> files -> List<FileResponse> files (uses FileMapper)
    FolderResponse toResponse(Folder folder);
}