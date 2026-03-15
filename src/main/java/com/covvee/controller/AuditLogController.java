package com.covvee.controller;

import com.covvee.dto.audit.AuditLogResponse;

import com.covvee.service.interfaces.AuditLogInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogInterface auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getRecentLogs() {
        return ResponseEntity.ok(auditLogService.getRecentLogs());
    }

    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByTargetId(@PathVariable String targetId) {
        return ResponseEntity.ok(auditLogService.getLogsByTargetId(targetId));
    }
}