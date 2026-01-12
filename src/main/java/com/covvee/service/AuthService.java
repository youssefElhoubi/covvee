package com.covvee.service;


import com.covvee.dto.auth.request.LoginRequest;
import com.covvee.dto.auth.request.RegisterRequest;
import com.covvee.dto.auth.response.AuthResponse;
import com.covvee.entity.User;
import com.covvee.enums.Role;
import com.covvee.mapper.UserMapper;
import com.covvee.repository.UserRepository;
import com.covvee.security.JwtUtils;
import com.covvee.service.interfaces.AuthInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateToken(authentication);

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        AuthResponse responseDto = userMapper.toAuthResponse(user);

        responseDto.setToken(token);

        return responseDto;
    }


    public AuthResponse signUp(RegisterRequest authRequestDto) {
        if (userRepository.findByEmail(authRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email already exists");
        }
        User user = userMapper.toEntity(authRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
        );

        // 4. Update Security Context (Optional, but good practice)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 5. Generate Token
        String token = jwtUtils.generateToken(authentication);

        // 6. Return Response
        AuthResponse response = userMapper.toAuthResponse(user);
        response.setToken(token);

        return response;
    }
}
