package com.example.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
    private LocalDate joinDate;
    private Long userId;
    private String username;
    private String role;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionTitle;
    private String phoneNumber;
    private String address;
}
