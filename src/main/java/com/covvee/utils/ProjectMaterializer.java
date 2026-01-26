package com.covvee.utils;

import com.covvee.entity.File;
import com.covvee.entity.Folder;
import com.covvee.repository.FileRepository;
import com.covvee.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMaterializer {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public void materializeProject(String projectId, Path destination) throws IOException {

        // 1. Bulk Fetch (Efficiency Rule #1)
        List<File> files = fileRepository.findByProjectId(projectId);
        List<Folder> folders = folderRepository.findByProjectId(projectId);

        // 2. Create Lookup Map (Efficiency Rule #2)
        // Allows us to find a folder by ID instantly.
        Map<String, Folder> folderMap = folders.stream()
                .collect(Collectors.toMap(Folder::getId, folder -> folder));

        // 3. Process every file
        for (File file : files) {
            // A. Build the relative path string (e.g., "src/main/app.java")
            String relativePath = buildPath(file.getParentId(), file.getName(), folderMap);

            // B. Resolve full path on disk (e.g., "/tmp/exec-123/src/main/app.java")
            Path targetFilePath = destination.resolve(relativePath);

            // C. Create parent directories on disk if they don't exist
            if (targetFilePath.getParent() != null) {
                Files.createDirectories(targetFilePath.getParent());
            }

            // D. Write content
            // NOTE: If you store Base64, decode it here!
            // If you store raw text, write it directly.
            Files.writeString(targetFilePath, file.getContent());
        }
    }
    private String buildPath(String parentId, String fileName, Map<String, Folder> folderMap) {
        // Start with the file name
        StringBuilder pathBuilder = new StringBuilder(fileName);

        String currentParentId = parentId;

        // Walk up the tree until we hit the root (null)
        while (currentParentId != null) {
            Folder parent = folderMap.get(currentParentId);

            // Safety check: If parent ID exists in File but missing in Map (Data corruption)
            if (parent == null) break;

            // Prepend folder name: "folder/" + "current_path"
            pathBuilder.insert(0, parent.getName() + "/");

            // Move up
            currentParentId = parent.getParentId();
        }

        return pathBuilder.toString();
    }
}
