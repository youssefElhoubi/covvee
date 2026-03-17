package com.covvee.repository;

import com.covvee.dto.admin.LanguageStatResponse;
import com.covvee.entity.Project;
import com.covvee.entity.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectRepository extends MongoRepository<Project,String> {

    List<Project> findAllByUserIdIn(List<String> userId);
    List<Project> findByUserIn(List<User> users);
    List<Project> findByUser(User user);
    boolean existsByIdAndUser(String id, User user);
    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$language', 'count': { '$sum': 1 } } }",
            "{ '$project': { 'language': '$_id', 'count': 1, '_id': 0 } }"
    })
    List<LanguageStatResponse> getLanguageStatistics();

    Map<String, Project> findAllByUserId(String userId);
}
