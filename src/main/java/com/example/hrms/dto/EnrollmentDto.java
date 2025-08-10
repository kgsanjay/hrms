package com.example.hrms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentDto {
    private Long id;
    private Long employeeId;
    private Long trainingProgramId;
    private LocalDate enrollmentDate;
}
