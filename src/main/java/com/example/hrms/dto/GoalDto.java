package com.example.hrms.dto;

import com.example.hrms.entity.enums.GoalStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GoalDto {
    private Long id;
    private Long employeeId;
    private String description;
    private LocalDate targetDate;
    private GoalStatus status;
}
