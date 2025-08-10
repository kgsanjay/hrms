package com.example.hrms.service;

import com.example.hrms.dto.*;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.OnboardingTask;
import com.example.hrms.entity.OffboardingTask;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.OnboardingTaskMapper;
import com.example.hrms.mapper.OffboardingTaskMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.OnboardingTaskRepository;
import com.example.hrms.repository.OffboardingTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing onboarding and offboarding tasks.
 */
@Service
@RequiredArgsConstructor
public class OnboardingOffboardingService {

    private final OnboardingTaskRepository onboardingTaskRepository;
    private final OffboardingTaskRepository offboardingTaskRepository;
    private final EmployeeRepository employeeRepository;
    private final OnboardingTaskMapper onboardingTaskMapper;
    private final OffboardingTaskMapper offboardingTaskMapper;

    /**
     * Creates a new onboarding task.
     * @param request the request object containing the task details
     * @return the created onboarding task
     */
    @Transactional
    public OnboardingTaskDto createOnboardingTask(CreateOnboardingTaskRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        OnboardingTask task = OnboardingTask.builder()
                .employee(employee)
                .taskDescription(request.getTaskDescription())
                .completed(false)
                .build();
        return onboardingTaskMapper.toDto(onboardingTaskRepository.save(task));
    }

    /**
     * Creates a new offboarding task.
     * @param request the request object containing the task details
     * @return the created offboarding task
     */
    @Transactional
    public OffboardingTaskDto createOffboardingTask(CreateOffboardingTaskRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        OffboardingTask task = OffboardingTask.builder()
                .employee(employee)
                .taskDescription(request.getTaskDescription())
                .completed(false)
                .build();
        return offboardingTaskMapper.toDto(offboardingTaskRepository.save(task));
    }
}
