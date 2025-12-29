package com.covvee.repository;

import com.covvee.entity.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FolderRepository extends MongoRepository<Folder,String> {
}
