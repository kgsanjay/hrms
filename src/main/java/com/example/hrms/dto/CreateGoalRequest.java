package com.example.hrms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateGoalRequest {
    private Long employeeId;
    private String description;
    private LocalDate targetDate;
}
