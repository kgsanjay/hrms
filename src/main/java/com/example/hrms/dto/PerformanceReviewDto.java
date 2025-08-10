package com.example.hrms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PerformanceReviewDto {
    private Long id;
    private Long employeeId;
    private Long reviewerId;
    private LocalDate reviewDate;
    private String comments;
    private List<GoalDto> goals;
}
