package com.example.hrms.service;

import com.example.hrms.dto.*;
import com.example.hrms.entity.*;
import com.example.hrms.entity.enums.GoalStatus;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.GoalMapper;
import com.example.hrms.mapper.PerformanceReviewMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.GoalRepository;
import com.example.hrms.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing performance reviews and goals.
 */
@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final GoalRepository goalRepository;
    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRepository employeeRepository;
    private final GoalMapper goalMapper;
    private final PerformanceReviewMapper performanceReviewMapper;

    /**
     * Creates a new goal for an employee.
     * @param request the request object containing the goal details
     * @return the created goal
     */
    @Transactional
    public GoalDto createGoal(CreateGoalRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Goal goal = Goal.builder()
                .employee(employee)
                .description(request.getDescription())
                .targetDate(request.getTargetDate())
                .status(GoalStatus.PENDING)
                .build();
        return goalMapper.toDto(goalRepository.save(goal));
    }

    /**
     * Updates a goal.
     * @param id the ID of the goal to update
     * @param request the request object containing the updated details
     * @return the updated goal
     */
    @Transactional
    public GoalDto updateGoal(Long id, UpdateGoalRequest request) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        if (request.getDescription() != null) {
            goal.setDescription(request.getDescription());
        }
        if (request.getTargetDate() != null) {
            goal.setTargetDate(request.getTargetDate());
        }
        if (request.getStatus() != null) {
            goal.setStatus(request.getStatus());
        }
        return goalMapper.toDto(goalRepository.save(goal));
    }

    /**
     * Creates a new performance review.
     * @param request the request object containing the performance review details
     * @return the created performance review
     */
    @Transactional
    public PerformanceReviewDto createPerformanceReview(CreatePerformanceReviewRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employee reviewer = employeeRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));
        List<Goal> goals = goalRepository.findAllById(request.getGoalIds());

        PerformanceReview review = PerformanceReview.builder()
                .employee(employee)
                .reviewer(reviewer)
                .reviewDate(request.getReviewDate())
                .comments(request.getComments())
                .goals(goals)
                .build();
        return performanceReviewMapper.toDto(performanceReviewRepository.save(review));
    }
}
