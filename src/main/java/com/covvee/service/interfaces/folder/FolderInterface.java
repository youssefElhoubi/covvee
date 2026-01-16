package com.covvee.service.interfaces.folder;

import com.covvee.dto.folder.request.CreateFolderRequest;
import com.covvee.dto.folder.response.FolderResponse;

import java.util.List;

public interface FolderInterface {

    /**
     * Creates a new folder.
     * Logic: Checks if a folder with the same name already exists in the target parentId.
     * @param request Contains name, projectId, and parentFolderId.
     * @return The created folder metadata.
     */
    FolderResponse createFolder(CreateFolderRequest request);

    /**
     * Retrieves a folder and its immediate children (Files and Sub-folders).
     * Used for "Lazy Loading" (when a user clicks the arrow > next to a folder).
     * @param folderId The unique ID of the folder.
     * @return The folder details with lists of direct children.
     */
    FolderResponse getFolderById(String folderId);

    /**
     * Renames a folder.
     * Logic: Must check for name collisions in the current directory.
     * @param folderId The ID of the folder.
     * @param newName The new name.
     * @return Updated folder response.
     */
    FolderResponse renameFolder(String folderId, String newName);

    /**
     * Moves a folder to a new location.
     * ⚠️ CRITICAL VALIDATION: Must prevent "Cyclic Moves" (e.g., you cannot move
     * Folder A inside Folder B if Folder B is actually inside Folder A).
     * @param folderId The folder to move.
     * @param newParentId The destination folder ID (or null for root).
     * @return Updated folder response.
     */
    FolderResponse moveFolder(String folderId, String newParentId);

    /**
     * Deletes a folder and ALL its contents recursively.
     * Logic:
     * 1. Find all files with parentId == folderId -> Delete them.
     * 2. Find all sub-folders with parentId == folderId.
     * 3. Recursively call deleteFolder() on those sub-folders.
     * 4. Finally, delete the folder itself.
     * @param folderId The ID of the folder to delete.
     */
    void deleteFolder(String folderId);

    /**
     * Internal/Advanced: Fetches all folders for a project.
     * Used by the ProjectService to build the initial tree structure.
     * @param projectId The project ID.
     * @return A flat list of all folders (the Tree Builder logic turns this into a hierarchy).
     */
    List<FolderResponse> getAllFoldersByProjectId(String projectId);
}
