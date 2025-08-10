package com.example.hrms.dto;

import com.example.hrms.entity.LeaveType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeaveRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LeaveType leaveType;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
