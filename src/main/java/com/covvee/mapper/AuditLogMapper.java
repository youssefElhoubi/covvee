package com.covvee.mapper;

import com.covvee.dto.audit.AuditLogResponse;
import com.covvee.dto.audit.CreateAuditLogDto;
import com.covvee.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    // 1. Entity -> Response (For viewing logs on the dashboard)
    AuditLogResponse toResponse(AuditLog auditLog);

    // 2. Internal DTO -> Entity (For saving a new log)
    @Mapping(target = "id", ignore = true) // MongoDB generates this
    @Mapping(target = "timestamp", ignore = true) // @CreatedDate generates this
    AuditLog toEntity(CreateAuditLogDto dto);
}