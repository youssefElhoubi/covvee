package com.covvee.service.interfaces;
import com.covvee.dto.admin.LanguageStatResponse;
import com.covvee.dto.admin.SystemHealthResponse;
import com.covvee.dto.auth.response.UserResponse;
import com.covvee.dto.user.response.UserResponseDTo;
import org.springframework.data.domain.Page;
import java.util.List;

public interface AdminServiceInterface {
    Page<UserResponseDTo> getPaginatedUsers(int page, int size);
    void banUser(String userId, String reason);
    void unbanUser(String userId, String reason);
    Page<UserResponseDTo> searchUsers(String keyword, int page, int size);
    SystemHealthResponse getSystemHealth();
    List<LanguageStatResponse> getLanguageAnalytics();
}
