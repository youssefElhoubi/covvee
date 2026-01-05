package com.covvee.repository;

import com.covvee.entity.Project;
import com.covvee.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project,String> {
    List<Project> findByUser(User user);
}
