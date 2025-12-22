package com.covvee.dto.auth.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;        // JWT Access Token
    private String refreshToken; // JWT Refresh Token
    private String type;         // Usually "Bearer"
    private UserResponse user;   // Nested user details
}