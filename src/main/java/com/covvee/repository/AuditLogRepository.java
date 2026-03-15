package com.covvee.repository;

import com.covvee.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findTop50ByOrderByTimestampDesc();
    List<AuditLog> findByTargetIdOrderByTimestampDesc(String targetId);
}
