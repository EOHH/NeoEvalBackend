package com.neoeval.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PracticeCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private Long groupId;
    private String groupName;
    private Long teacherId;
}
