package com.example.hrms.controller;

import com.example.hrms.dto.*;
import com.example.hrms.service.OnboardingOffboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OnboardingOffboardingController {

    private final OnboardingOffboardingService onboardingOffboardingService;

    @PostMapping("/onboarding-tasks")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<OnboardingTaskDto> createOnboardingTask(@RequestBody CreateOnboardingTaskRequest request) {
        return new ResponseEntity<>(onboardingOffboardingService.createOnboardingTask(request), HttpStatus.CREATED);
    }

    @PostMapping("/offboarding-tasks")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<OffboardingTaskDto> createOffboardingTask(@RequestBody CreateOffboardingTaskRequest request) {
        return new ResponseEntity<>(onboardingOffboardingService.createOffboardingTask(request), HttpStatus.CREATED);
    }
}
