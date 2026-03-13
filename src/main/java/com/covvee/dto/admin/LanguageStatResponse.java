package com.covvee.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageStatResponse {
    private String language; // e.g., "PYTHON"
    private long count;      // e.g., 150
}