package com.example.hrms.service;

import com.example.hrms.dto.AttendanceDto;
import com.example.hrms.dto.LeaveRequestDto;
import com.example.hrms.entity.Employee;
import com.example.hrms.mapper.AttendanceMapper;
import com.example.hrms.mapper.LeaveRequestMapper;
import com.example.hrms.repository.AttendanceRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating reports.
 */
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;
    private final LeaveRequestMapper leaveRequestMapper;

    /**
     * Generates an attendance report for a given department and date range.
     * @param departmentId the ID of the department
     * @param startDate the start date of the report
     * @param endDate the end date of the report
     * @return a list of attendance records
     */
    @Transactional(readOnly = true)
    public List<AttendanceDto> generateAttendanceReport(Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<Employee> employees = employeeRepository.findAllByDepartmentId(departmentId);
        List<Long> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

        return attendanceRepository.findByEmployeeIdInAndDateBetween(employeeIds, startDate, endDate)
                .stream()
                .map(attendanceMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Generates a leave report for a given department and date range.
     * @param departmentId the ID of the department
     * @param startDate the start date of the report
     * @param endDate the end date of the report
     * @return a list of leave requests
     */
    @Transactional(readOnly = true)
    public List<LeaveRequestDto> generateLeaveReport(Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<Employee> employees = employeeRepository.findAllByDepartmentId(departmentId);
        List<Long> employeeIds = employees.stream().map(Employee::getId).collect(Collectors.toList());

        return leaveRequestRepository.findByEmployeeIdInAndStartDateBetween(employeeIds, startDate, endDate)
                .stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
