package com.covvee.repository;

import com.covvee.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Spring reads this as:
    // "Find" -> SELECT
    // "ByUsername" -> WHERE username
    // "ContainingIgnoreCase" -> LIKE %keyword% (case insensitive)
    // "Or" -> OR
    // "Email" -> email
    // "ContainingIgnoreCase" -> LIKE %keyword% (case insensitive)
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);
    Optional<User> findByEmail(String email);
}
