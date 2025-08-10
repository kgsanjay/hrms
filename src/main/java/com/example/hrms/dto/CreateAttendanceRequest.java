package com.example.hrms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttendanceRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate date;
    private LocalTime checkInTime;
}
