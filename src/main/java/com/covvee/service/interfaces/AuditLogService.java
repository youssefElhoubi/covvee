package com.covvee.service.interfaces;

import com.covvee.dto.audit.AuditLogResponse;
import com.covvee.dto.audit.CreateAuditLogDto;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface AuditLogService {
    void logAction(@Valid CreateAuditLogDto request);

    List<AuditLogResponse> getRecentLogs();

    List<AuditLogResponse> getLogsByTargetId(String targetId);
}