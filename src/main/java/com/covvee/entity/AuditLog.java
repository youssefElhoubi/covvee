package com.covvee.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    private String id;

    private String adminId;       // Who did it?
    private String adminUsername; // For easy reading on the dashboard

    private String action;        // e.g., "BANNED_USER", "DELETED_PROJECT"
    private String targetId;      // The ID of the user or project that was affected
    private String details;       // e.g., "Banned user for mining crypto"

    @CreatedDate
    private LocalDateTime timestamp;
}
