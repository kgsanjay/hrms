package com.example.hrms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTrainingProgramRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxCapacity;
}
