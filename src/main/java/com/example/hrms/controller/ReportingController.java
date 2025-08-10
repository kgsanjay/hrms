package com.example.hrms.controller;

import com.example.hrms.dto.AttendanceDto;
import com.example.hrms.dto.LeaveRequestDto;
import com.example.hrms.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/attendance")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<List<AttendanceDto>> getAttendanceReport(
            @RequestParam Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateAttendanceReport(departmentId, startDate, endDate));
    }

    @GetMapping("/leave")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<List<LeaveRequestDto>> getLeaveReport(
            @RequestParam Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateLeaveReport(departmentId, startDate, endDate));
    }
}
