package com.covvee.service;

import com.covvee.dto.admin.LanguageStatResponse;
import com.covvee.dto.admin.SystemHealthResponse;
import com.covvee.dto.auth.response.UserResponse;
import com.covvee.dto.user.response.UserResponseDTo;
import com.covvee.entity.User;
import com.covvee.mapper.UserMapper;
import com.covvee.repository.UserRepository;
import com.covvee.service.interfaces.AdminServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminServiceInterface {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponseDTo> getPaginatedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toBaseUserResponse);
    }
    @Override
    public void banUser(String userId, String reason) {

    }

    @Override
    public void unbanUser(String userId, String reason) {

    }

    @Override
    public List<UserResponse> searchUsers(String keyword) {
        return List.of();
    }

    @Override
    public SystemHealthResponse getSystemHealth() {
        return null;
    }

    @Override
    public List<LanguageStatResponse> getLanguageAnalytics() {
        return List.of();
    }
}
