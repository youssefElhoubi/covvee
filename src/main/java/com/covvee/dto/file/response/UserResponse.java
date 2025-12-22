package com.covvee.dto.file.response;

import com.covvee.enums.Role;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String avatarUrl;
    private Role role;
    private LocalDateTime joinedAt; // Mapped from createdAt
}