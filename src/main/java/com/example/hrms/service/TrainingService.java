package com.example.hrms.service;

import com.example.hrms.dto.CreateEnrollmentRequest;
import com.example.hrms.dto.CreateTrainingProgramRequest;
import com.example.hrms.dto.EnrollmentDto;
import com.example.hrms.dto.TrainingProgramDto;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.Enrollment;
import com.example.hrms.entity.TrainingProgram;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.EnrollmentMapper;
import com.example.hrms.mapper.TrainingProgramMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.EnrollmentRepository;
import com.example.hrms.repository.TrainingProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service for managing training programs and enrollments.
 */
@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingProgramRepository trainingProgramRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TrainingProgramMapper trainingProgramMapper;
    private final EnrollmentMapper enrollmentMapper;

    /**
     * Creates a new training program.
     * @param request the request object containing the training program details
     * @return the created training program
     */
    @Transactional
    public TrainingProgramDto createTrainingProgram(CreateTrainingProgramRequest request) {
        TrainingProgram program = TrainingProgram.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .maxCapacity(request.getMaxCapacity())
                .build();
        return trainingProgramMapper.toDto(trainingProgramRepository.save(program));
    }

    /**
     * Enrolls an employee in a training program.
     * @param request the request object containing the enrollment details
     * @return the created enrollment
     */
    @Transactional
    public EnrollmentDto createEnrollment(CreateEnrollmentRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        TrainingProgram program = trainingProgramRepository.findById(request.getTrainingProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Training program not found"));

        Enrollment enrollment = Enrollment.builder()
                .employee(employee)
                .trainingProgram(program)
                .enrollmentDate(LocalDate.now())
                .build();
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }
}
