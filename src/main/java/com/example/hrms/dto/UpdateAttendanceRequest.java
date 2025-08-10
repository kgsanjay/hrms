package com.example.hrms.dto;

import com.example.hrms.entity.enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateAttendanceRequest {
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;
}
