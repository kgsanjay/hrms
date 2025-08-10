package com.example.hrms.dto;

import com.example.hrms.entity.enums.GoalStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateGoalRequest {
    private String description;
    private LocalDate targetDate;
    private GoalStatus status;
}
