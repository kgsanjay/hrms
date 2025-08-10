package com.example.hrms.service;

import com.example.hrms.dto.CreateLeaveRequest;
import com.example.hrms.dto.LeaveRequestDto;
import com.example.hrms.dto.UpdateLeaveRequestStatusRequest;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.LeaveRequest;
import com.example.hrms.entity.LeaveRequestStatus;
import com.example.hrms.mapper.LeaveRequestMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.LeaveRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing leave requests.
 */
@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, LeaveRequestMapper leaveRequestMapper) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveRequestMapper = leaveRequestMapper;
    }

    /**
     * Creates a new leave request.
     * @param request the request object containing the leave request details
     * @return the created leave request
     */
    public LeaveRequestDto createLeaveRequest(CreateLeaveRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(request.getLeaveType());
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);

        return leaveRequestMapper.toDto(leaveRequestRepository.save(leaveRequest));
    }

    /**
     * Updates the status of a leave request.
     * @param id the ID of the leave request to update
     * @param request the request object containing the new status
     * @return the updated leave request
     */
    public LeaveRequestDto updateLeaveRequestStatus(Long id, UpdateLeaveRequestStatusRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        leaveRequest.setStatus(request.getStatus());
        return leaveRequestMapper.toDto(leaveRequestRepository.save(leaveRequest));
    }

    /**
     * Retrieves all leave requests.
     * @return a list of all leave requests
     */
    public List<LeaveRequestDto> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
