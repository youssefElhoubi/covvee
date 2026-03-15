package com.covvee.service;

import com.covvee.dto.admin.LanguageStatResponse;
import com.covvee.dto.admin.SystemHealthResponse;
import com.covvee.dto.user.response.UserResponseDTo;
import com.covvee.entity.User;
import com.covvee.execption.ResourceNotFoundException;
import com.covvee.mapper.UserMapper;
import com.covvee.repository.ProjectRepository;
import com.covvee.repository.UserRepository;
import com.covvee.service.interfaces.AdminServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements AdminServiceInterface {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserResponseDTo> getPaginatedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toBaseUserResponse);
    }

    @Override
    public void banUser(String userId, String reason) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setBanned(true);
        user.setBanReason(reason);
        userRepository.save(user);
    }

    @Override
    public void unbanUser(String userId, String reason) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setBanned(false);
        user.setBanReason(reason);
        userRepository.save(user);
    }

    @Override
    public Page<UserResponseDTo> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable).map(userMapper::toBaseUserResponse);
        }
        Page<User> searchResults = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword,
                keyword,
                pageable
        );
        return searchResults.map(userMapper::toBaseUserResponse);
    }

    @Override
    public SystemHealthResponse getSystemHealth() {

        // 1. Find the temporary directory used by the OS and Java
        File tempDir = new File(System.getProperty("java.io.tmpdir"));

        // 2. Ask the OS for the exact byte counts
        long totalSpace = tempDir.getTotalSpace();
        long freeSpace = tempDir.getUsableSpace(); // usable is safer than freeSpace
        long usedSpace = totalSpace - freeSpace;

        // 3. Calculate the percentage used
        double usedPercentage = 0.0;
        if (totalSpace > 0) {
            usedPercentage = ((double) usedSpace / totalSpace) * 100;
        }

        // 4. Determine the server health status
        String status = "HEALTHY";
        if (usedPercentage >= 95.0) {
            status = "CRITICAL"; // Danger! The server might crash soon
        } else if (usedPercentage >= 80.0) {
            status = "WARNING";  // Time to clean up old temp files
        }

        // 5. Build and return the DTO
        return SystemHealthResponse.builder()
                .status(status)
                .totalDiskSpaceBytes(totalSpace)
                .freeDiskSpaceBytes(freeSpace)
                // Round to 2 decimal places for a cleaner UI (e.g., 45.67%)
                .usedSpacePercentage(Math.round(usedPercentage * 100.0) / 100.0)
                .build();
    }

    @Override
    public List<LanguageStatResponse> getLanguageAnalytics() {
        return projectRepository.getLanguageStatistics();
    }
}
