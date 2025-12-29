package com.covvee.dto.user.request;

import com.covvee.enums.Role;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class userRequest {
    @NotBlank(message = "id is required")
    private String id;
    private String name;
    private String password;
    private String avatarUrl;
}
