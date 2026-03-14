package com.covvee.service.interfaces;
import com.covvee.dto.admin.LanguageStatResponse;
import com.covvee.dto.admin.SystemHealthResponse;
import com.covvee.dto.auth.response.UserResponse;
import java.util.List;
public interface AdminService {
    void banUser(String userId, String reason);
    void unbanUser(String userId, String reason);
    List<UserResponse> searchUsers(String keyword);
    SystemHealthResponse getSystemHealth();
    List<LanguageStatResponse> getLanguageAnalytics();
}
