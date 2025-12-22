package com.covvee.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

public abstract class FileSystemNode {
    @Id
    private String id;

    @Indexed // Speed up lookups by project
    private String projectId;

    @Indexed // Speed up lookups by folder
    private String parentId; // Null if it's at the root (top level) of the project

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
