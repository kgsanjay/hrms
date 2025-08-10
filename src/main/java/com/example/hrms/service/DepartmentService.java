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

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public DepartmentDto createDepartment(CreateUpdateDepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDto(department);
    }

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto updateDepartment(Long id, CreateUpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(updatedDepartment);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
