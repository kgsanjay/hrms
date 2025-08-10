package com.example.hrms.service;

import com.example.hrms.dto.AttendanceDto;
import com.example.hrms.dto.CreateAttendanceRequest;
import com.example.hrms.dto.UpdateAttendanceRequest;
import com.example.hrms.entity.Attendance;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.enums.AttendanceStatus;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.AttendanceMapper;
import com.example.hrms.repository.AttendanceRepository;
import com.example.hrms.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    /**
     * Records an employee's clock-in time.
     * @param request the request object containing the clock-in details
     * @return the created attendance record
     */
    @Transactional
    public AttendanceDto clockIn(CreateAttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        // Check if already clocked in today
        attendanceRepository.findByEmployeeIdAndDate(request.getEmployeeId(), request.getDate())
                .ifPresent(a -> {
                    throw new IllegalStateException("Employee has already clocked in today.");
                });

        Attendance attendance = attendanceMapper.toEntity(request, employee);
        attendance.setStatus(AttendanceStatus.PRESENT);
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(savedAttendance);
    }

    /**
     * Records an employee's clock-out time.
     * @param employeeId the ID of the employee
     * @param date the date of the attendance record
     * @return the updated attendance record
     */
    @Transactional
    public AttendanceDto clockOut(Long employeeId, LocalDate date) {
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found for employee " + employeeId + " on " + date));

        if (attendance.getCheckOut() != null) {
            throw new IllegalStateException("Employee has already clocked out today.");
        }

        attendance.setCheckOut(java.time.LocalTime.now());
        Attendance updatedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(updatedAttendance);
    }

    /**
     * Retrieves the attendance records for an employee within a given date range.
     * @param employeeId the ID of the employee
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of attendance records
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> getEmployeeAttendance(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        return attendances.stream()
                .map(attendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an attendance record.
     * @param id the ID of the attendance record to update
     * @param request the request object containing the updated details
     * @return the updated attendance record
     */
    @Transactional
    public AttendanceDto updateAttendance(Long id, UpdateAttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));

        if (request.getCheckInTime() != null) {
            attendance.setCheckIn(request.getCheckInTime());
        }
        if (request.getCheckOutTime() != null) {
            attendance.setCheckOut(request.getCheckOutTime());
        }
        if (request.getStatus() != null) {
            attendance.setStatus(request.getStatus());
        }

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(updatedAttendance);
    }

    /**
     * Retrieves an attendance record by its ID.
     * @param id the ID of the attendance record
     * @return the attendance record
     */
    @Transactional(readOnly = true)
    public AttendanceDto getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
        return attendanceMapper.toDto(attendance);
    }
}
