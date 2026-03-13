package com.covvee.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {
    private String id;
    private String adminId;
    private String adminUsername;
    private String action;
    private String targetId;
    private String details;
    private LocalDateTime timestamp;
}