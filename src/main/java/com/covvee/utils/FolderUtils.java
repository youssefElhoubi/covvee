package com.covvee.utils;

import com.covvee.entity.Folder;
import com.covvee.repository.FileRepository;
import com.covvee.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FolderUtils {
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public void childrenSafeRemove(List<Folder> folders){
        if (folders == null || folders.isEmpty()) return;

        for (Folder f : folders) {
            if (f.getChildren() != null && !f.getChildren().isEmpty()) {
                childrenSafeRemove(f.getChildren());
            }
            if (f.getFiles() != null && !f.getFiles().isEmpty()) {
                fileRepository.deleteAll(f.getFiles());
            }
            folderRepository.delete(f);
        }
    }
}