package com.covvee.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemHealthResponse {
    private String status;               // e.g., "HEALTHY", "WARNING", "CRITICAL"
    private long totalDiskSpaceBytes;    // The size of your server's drive
    private long freeDiskSpaceBytes;     // How much room is left
    private double usedSpacePercentage;  // Easy percentage for a dashboard progress bar
}