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

    public LeaveRequestDto updateLeaveRequestStatus(Long id, UpdateLeaveRequestStatusRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        leaveRequest.setStatus(request.getStatus());
        return leaveRequestMapper.toDto(leaveRequestRepository.save(leaveRequest));
    }

    public List<LeaveRequestDto> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
