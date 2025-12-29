package com.covvee.mapper;

import com.covvee.dto.auth.request.RegisterRequest;
import com.covvee.dto.auth.response.AuthResponse;
import com.covvee.dto.auth.response.UserResponse;
import com.covvee.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity -> Response (Hide password)
    @Mapping(target = "joinedAt", source = "createdAt")
    UserResponse toResponse(User user);
    @Mapping(target="token" , ignore = true)
    @Mapping(target="refreshToken" , ignore = true)
    AuthResponse  toAuthResponse(User user);

    // Request -> Entity (Password encoding happens in Service, not here)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER") // Default role
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    User toEntity(RegisterRequest request);
}