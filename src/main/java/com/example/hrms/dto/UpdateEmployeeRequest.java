package com.example.hrms.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {
    @NotEmpty(message = "Employee name is required")
    private String name;

    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Position ID is required")
    private Long positionId;
}
