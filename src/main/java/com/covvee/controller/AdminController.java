package com.covvee.controller;

import com.covvee.dto.admin.LanguageStatResponse;
import com.covvee.dto.admin.SystemHealthResponse;
import com.covvee.dto.user.response.UserResponseDTo;
import com.covvee.service.interfaces.AdminServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceInterface adminService;

    // 1. Get All Users OR Search Users
    // Example: GET /api/admin/users?keyword=youssef&page=0&size=10
    // Example: GET /api/admin/users?page=1 (Defaults to empty keyword)
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTo>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(adminService.searchUsers(keyword, page, size));
    }

    // 2. Ban a User
    // Example: PATCH /api/admin/users/123/ban?reason=Spamming
    @PatchMapping("/users/{userId}/ban")
    public ResponseEntity<Void> banUser(
            @PathVariable String userId,
            @RequestParam String reason) {

        adminService.banUser(userId, reason);
        return ResponseEntity.ok().build(); // Returns 200 OK with no body
    }

    // 3. Unban a User
    // Example: PATCH /api/admin/users/123/unban?reason=Apologized
    @PatchMapping("/users/{userId}/unban")
    public ResponseEntity<Void> unbanUser(
            @PathVariable String userId,
            @RequestParam String reason) {

        adminService.unbanUser(userId, reason);
        return ResponseEntity.ok().build();
    }

    // 4. Get System Health
    // Example: GET /api/admin/health
    @GetMapping("/health")
    public ResponseEntity<SystemHealthResponse> getSystemHealth() {
        return ResponseEntity.ok(adminService.getSystemHealth());
    }

    // 5. Get Language Analytics
    // Example: GET /api/admin/analytics/languages
    @GetMapping("/analytics/languages")
    public ResponseEntity<List<LanguageStatResponse>> getLanguageAnalytics() {
        return ResponseEntity.ok(adminService.getLanguageAnalytics());
    }
}