package com.example.hrms.service;

import com.example.hrms.dto.CreateUpdateDepartmentRequest;
import com.example.hrms.dto.DepartmentDto;
import com.example.hrms.entity.Department;
import com.example.hrms.mapper.DepartmentMapper;
import com.example.hrms.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing departments.
 */
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Creates a new department.
     * @param request the request object containing the department details
     * @return the created department
     */
    public DepartmentDto createDepartment(CreateUpdateDepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    /**
     * Retrieves a department by its ID.
     * @param id the ID of the department to retrieve
     * @return the department
     */
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDto(department);
    }

    /**
     * Retrieves all departments.
     * @return a list of all departments
     */
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates a department's details.
     * @param id the ID of the department to update
     * @param request the request object containing the updated details
     * @return the updated department
     */
    public DepartmentDto updateDepartment(Long id, CreateUpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(updatedDepartment);
    }

    /**
     * Deletes a department by its ID.
     * @param id the ID of the department to delete
     */
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
