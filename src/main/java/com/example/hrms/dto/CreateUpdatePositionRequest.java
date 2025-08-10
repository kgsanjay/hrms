package com.example.hrms.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdatePositionRequest {
    @NotEmpty(message = "Position title is required")
    private String title;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
