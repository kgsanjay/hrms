package com.example.hrms.controller;

import com.example.hrms.dto.*;
import com.example.hrms.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping("/goals")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<GoalDto> createGoal(@RequestBody CreateGoalRequest request) {
        return new ResponseEntity<>(performanceService.createGoal(request), HttpStatus.CREATED);
    }

    @PutMapping("/goals/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable Long id, @RequestBody UpdateGoalRequest request) {
        return ResponseEntity.ok(performanceService.updateGoal(id, request));
    }

    @PostMapping("/performance-reviews")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<PerformanceReviewDto> createPerformanceReview(@RequestBody CreatePerformanceReviewRequest request) {
        return new ResponseEntity<>(performanceService.createPerformanceReview(request), HttpStatus.CREATED);
    }
}
