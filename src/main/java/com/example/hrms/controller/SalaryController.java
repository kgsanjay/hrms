package com.example.hrms.controller;

import com.example.hrms.dto.GenerateSalarySlipRequest;
import com.example.hrms.dto.SalarySlipDto;
import com.example.hrms.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping("/salaries/generate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<SalarySlipDto> generateSalarySlip(@RequestBody GenerateSalarySlipRequest request) {
        return new ResponseEntity<>(salaryService.generateSalarySlip(request), HttpStatus.CREATED);
    }

    @GetMapping("/employees/me/salary-slips")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SalarySlipDto>> getMySalarySlips(Authentication authentication) {
        return ResponseEntity.ok(salaryService.getMySalarySlips(authentication.getName()));
    }
}
