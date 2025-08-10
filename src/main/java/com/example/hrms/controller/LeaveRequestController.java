package com.example.hrms.controller;

import com.example.hrms.dto.CreateLeaveRequest;
import com.example.hrms.dto.LeaveRequestDto;
import com.example.hrms.dto.UpdateLeaveRequestStatusRequest;
import com.example.hrms.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequestDto> createLeaveRequest(@Valid @RequestBody CreateLeaveRequest request) {
        return new ResponseEntity<>(leaveRequestService.createLeaveRequest(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<LeaveRequestDto> updateLeaveRequestStatus(@PathVariable Long id, @Valid @RequestBody UpdateLeaveRequestStatusRequest request) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveRequestStatus(id, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<List<LeaveRequestDto>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }
}
