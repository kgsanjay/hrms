package com.example.hrms.service;

import com.example.hrms.dto.CreateEmployeeRequest;
import com.example.hrms.dto.EmployeeDto;
import com.example.hrms.dto.UpdateEmployeeRequest;
import com.example.hrms.entity.*;
import com.example.hrms.mapper.EmployeeMapper;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.dto.UpdateProfileRequest;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.PositionRepository;
import com.example.hrms.repository.RoleRepository;
import com.example.hrms.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
                           PositionRepository positionRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, EmployeeMapper employeeMapper, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeMapper = employeeMapper;
        this.userRepository = userRepository;
    }

    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new EntityNotFoundException("Position not found"));
        Role role = roleRepository.findByName(RoleType.valueOf(request.getRole()))
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setJoinDate(request.getJoinDate());
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setUser(user);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto updateEmployee(Long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new EntityNotFoundException("Position not found"));

        employee.setName(request.getName());
        employee.setJoinDate(request.getJoinDate());
        employee.setDepartment(department);
        employee.setPosition(position);

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Transactional
    public EmployeeDto updateMyProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Employee employee = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user: " + username));

        if (request.getPhoneNumber() != null) {
            employee.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }
}
