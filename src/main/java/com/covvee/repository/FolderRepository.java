package com.covvee.repository;

import com.covvee.entity.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends MongoRepository<Folder,String> {
    List<Folder> findByProjectId(String strings);
}
