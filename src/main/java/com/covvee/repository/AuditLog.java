package com.covvee.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLog extends MongoRepository<String,AuditLog> {
}
