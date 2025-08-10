package com.example.hrms.controller;

import com.example.hrms.dto.CreateEnrollmentRequest;
import com.example.hrms.dto.CreateTrainingProgramRequest;
import com.example.hrms.dto.EnrollmentDto;
import com.example.hrms.dto.TrainingProgramDto;
import com.example.hrms.service.TrainingService;
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
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping("/training-programs")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<TrainingProgramDto> createTrainingProgram(@RequestBody CreateTrainingProgramRequest request) {
        return new ResponseEntity<>(trainingService.createTrainingProgram(request), HttpStatus.CREATED);
    }

    @PostMapping("/enrollments")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<EnrollmentDto> createEnrollment(@RequestBody CreateEnrollmentRequest request) {
        return new ResponseEntity<>(trainingService.createEnrollment(request), HttpStatus.CREATED);
    }
}
