package com.covvee.repository;

import com.covvee.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<File,String> {
    List<File> findByProjectId(String projectId);
}
