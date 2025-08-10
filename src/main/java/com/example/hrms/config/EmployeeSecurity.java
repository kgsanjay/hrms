package com.example.hrms.config;

import com.example.hrms.repository.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("employeeSecurity")
public class EmployeeSecurity {

    private final EmployeeRepository employeeRepository;

    public EmployeeSecurity(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public boolean isOwner(Authentication authentication, Long employeeId) {
        String username = authentication.getName();
        return employeeRepository.findById(employeeId)
                .map(employee -> employee.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
