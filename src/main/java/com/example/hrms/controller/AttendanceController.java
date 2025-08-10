package com.example.hrms.controller;

import com.example.hrms.dto.AttendanceDto;
import com.example.hrms.dto.CreateAttendanceRequest;
import com.example.hrms.dto.UpdateAttendanceRequest;
import com.example.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/clock-in")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN', 'HR')")
    public ResponseEntity<AttendanceDto> clockIn(@RequestBody CreateAttendanceRequest request) {
        return new ResponseEntity<>(attendanceService.clockIn(request), HttpStatus.CREATED);
    }

    @PostMapping("/clock-out/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN', 'HR')")
    public ResponseEntity<AttendanceDto> clockOut(@PathVariable Long employeeId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.clockOut(employeeId, date));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR') or #employeeId == authentication.principal.id")
    public ResponseEntity<List<AttendanceDto>> getEmployeeAttendance(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getEmployeeAttendance(employeeId, startDate, endDate));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<AttendanceDto> updateAttendance(@PathVariable Long id, @RequestBody UpdateAttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, request));
    }
}
