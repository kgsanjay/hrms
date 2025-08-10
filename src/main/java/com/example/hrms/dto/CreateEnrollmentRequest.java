package com.example.hrms.dto;

import lombok.Data;

@Data
public class CreateEnrollmentRequest {
    private Long employeeId;
    private Long trainingProgramId;
}
