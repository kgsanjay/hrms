package com.example.hrms.service;

import com.example.hrms.dto.GenerateSalarySlipRequest;
import com.example.hrms.dto.SalarySlipDto;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.SalarySlip;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.SalarySlipMapper;
import com.example.hrms.entity.User;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.SalarySlipRepository;
import com.example.hrms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalarySlipRepository salarySlipRepository;
    private final EmployeeRepository employeeRepository;
    private final SalarySlipMapper salarySlipMapper;
    private final UserRepository userRepository;

    @Transactional
    public SalarySlipDto generateSalarySlip(GenerateSalarySlipRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        BigDecimal netPay = request.getBasicSalary().subtract(request.getDeductions());

        SalarySlip salarySlip = SalarySlip.builder()
                .employee(employee)
                .payPeriod(request.getPayPeriod())
                .basicSalary(request.getBasicSalary())
                .deductions(request.getDeductions())
                .netPay(netPay)
                .build();

        SalarySlip savedSlip = salarySlipRepository.save(salarySlip);
        return salarySlipMapper.toDto(savedSlip);
    }

    @Transactional(readOnly = true)
    public List<SalarySlipDto> getMySalarySlips(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        Employee employee = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user: " + username));

        List<SalarySlip> salarySlips = salarySlipRepository.findByEmployeeId(employee.getId());
        return salarySlips.stream()
                .map(salarySlipMapper::toDto)
                .collect(Collectors.toList());
    }
}
