package com.example.hrms.dto;

import lombok.Data;

@Data
public class OffboardingTaskDto {
    private Long id;
    private Long employeeId;
    private String taskDescription;
    private boolean completed;
}
