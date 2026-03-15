package com.covvee.service;
import com.covvee.dto.audit.AuditLogResponse;
import com.covvee.dto.audit.CreateAuditLogDto;
import com.covvee.entity.AuditLog;
import com.covvee.mapper.AuditLogMapper;
import com.covvee.repository.AuditLogRepository;
import com.covvee.service.interfaces.AuditLogInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Adds a standard Java logger
public class AuditLogService implements AuditLogInterface {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void logAction(CreateAuditLogDto request) {
        try {
            // 1. Map the validated DTO to the Entity
            AuditLog auditLog = auditLogMapper.toEntity(request);

            // 2. Save it to MongoDB
            auditLogRepository.save(auditLog);

            // 3. (Optional but good practice) Print to the server console
            log.info("ADMIN ACTION LOGGED: Admin [{}] performed [{}] on target [{}]",
                    request.getAdminUsername(), request.getAction(), request.getTargetId());

        } catch (Exception e) {
            // We catch the error so if the audit log fails to save,
            // it doesn't crash the main action (like banning the user).
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    @Override
    public List<AuditLogResponse> getRecentLogs() {
        // Fetch from DB and map to Response DTOs
        return auditLogRepository.findTop50ByOrderByTimestampDesc()
                .stream()
                .map(auditLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<AuditLogResponse> getLogsByTargetId(String targetId) {
        // Fetch from DB and map to Response DTOs
        return auditLogRepository.findByTargetIdOrderByTimestampDesc(targetId)
                .stream()
                .map(auditLogMapper::toResponse)
                .toList();
    }
}
