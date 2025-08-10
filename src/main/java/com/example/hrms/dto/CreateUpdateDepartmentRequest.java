package com.example.hrms.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateDepartmentRequest {
    @NotEmpty(message = "Department name is required")
    private String name;
    private String description;
}
