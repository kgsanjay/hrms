package com.example.hrms.controller;

import com.example.hrms.dto.CreateEmployeeRequest;
import com.example.hrms.dto.EmployeeDto;
import com.example.hrms.dto.UpdateEmployeeRequest;
import com.example.hrms.dto.UpdateProfileRequest;
import com.example.hrms.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return new ResponseEntity<>(employeeService.createEmployee(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR') or @employeeSecurity.isOwner(authentication, #id)")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeDto> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request, Authentication authentication) {
        return ResponseEntity.ok(employeeService.updateMyProfile(authentication.getName(), request));
    }
}
