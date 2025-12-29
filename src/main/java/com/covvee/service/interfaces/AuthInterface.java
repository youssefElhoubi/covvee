package com.covvee.service.interfaces;

import com.covvee.dto.auth.request.LoginRequest;
import com.covvee.dto.auth.request.RegisterRequest;
import com.covvee.dto.auth.response.AuthResponse;


public interface AuthInterface {
    AuthResponse signUp(RegisterRequest dto);
    AuthResponse login(LoginRequest dto);
}
