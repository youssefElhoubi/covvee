package com.covvee.dto.file.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {
    private String id;
    private String name;
    private String language;
    private String content;
    private String parentId;
}