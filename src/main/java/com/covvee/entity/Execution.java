package com.covvee.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Execution {
    @Id
    private String id;
    private String projectID;
    private String output;
    private String error;
    private int exitCode;
    private long executionTimeMs;
    private boolean isTimeout;
    @CreatedDate
    private LocalDateTime createdDate;
}
