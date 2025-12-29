package com.covvee.service.interfaces;

import com.covvee.dto.auth.request.LoginRequest;
import com.covvee.dto.auth.response.AuthResponse;
import com.covvee.dto.auth.response.UserResponse;

public interface AuthInterface {
    AuthResponse signUp(LoginRequest dto);
    AuthResponse login(LoginRequest dto);
}
