package com.example.hrms.dto;

import lombok.Data;

@Data
public class CreateOffboardingTaskRequest {
    private Long employeeId;
    private String taskDescription;
}
