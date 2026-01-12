package com.covvee.controller;

import com.covvee.dto.auth.request.LoginRequest;
import com.covvee.dto.auth.request.RegisterRequest;
import com.covvee.dto.auth.response.AuthResponse;
import com.covvee.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody RegisterRequest dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }


}
