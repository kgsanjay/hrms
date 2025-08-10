package com.example.hrms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreatePerformanceReviewRequest {
    private Long employeeId;
    private Long reviewerId;
    private LocalDate reviewDate;
    private String comments;
    private List<Long> goalIds;
}
