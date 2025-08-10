package com.example.hrms.dto;

import lombok.Data;

@Data
public class CreateOnboardingTaskRequest {
    private Long employeeId;
    private String taskDescription;
}
