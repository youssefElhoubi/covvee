package com.covvee.entity;


import com.covvee.enums.Language;
import com.covvee.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Project {
    @Id
    private String id;
    private String name;
    private String description;
    private Visibility visibility;
    private Language language;
    @DBRef
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAT;
}
