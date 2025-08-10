package com.example.hrms.controller;

import com.example.hrms.dto.GenerateSalarySlipRequest;
import com.example.hrms.entity.*;
import com.example.hrms.repository.*;
import com.example.hrms.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalarySlipRepository salarySlipRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private String adminToken;
    private String employeeToken;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        salarySlipRepository.deleteAll();
        documentRepository.deleteAll();
        attendanceRepository.deleteAll();
        leaveRequestRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        positionRepository.deleteAll();
        departmentRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = roleRepository.save(Role.builder().name(RoleType.ADMIN).build());
        User adminUser = User.builder().username("admin").password(passwordEncoder.encode("password")).role(adminRole).status(UserStatus.ACTIVE).build();
        userRepository.save(adminUser);
        adminToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin"));

        Role employeeRole = roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());
        User employeeUser = User.builder().username("employee").password(passwordEncoder.encode("password")).role(employeeRole).status(UserStatus.ACTIVE).build();
        userRepository.save(employeeUser);
        employeeToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("employee"));

        Department department = departmentRepository.save(new Department(null, "IT", null));
        Position position = positionRepository.save(new Position(null, "Developer", department));
        testEmployee = employeeRepository.save(Employee.builder()
                .user(employeeUser)
                .name("Test Employee")
                .department(department)
                .position(position)
                .joinDate(LocalDate.now())
                .build());
    }

    @Test
    void shouldGenerateSalarySlipWhenUserIsAdmin() throws Exception {
        GenerateSalarySlipRequest request = new GenerateSalarySlipRequest();
        request.setEmployeeId(testEmployee.getId());
        request.setPayPeriod(YearMonth.of(2025, 10));
        request.setBasicSalary(new BigDecimal("5000.00"));
        request.setDeductions(new BigDecimal("500.00"));

        mockMvc.perform(post("/api/salaries/generate")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.netPay").value(4500.00));
    }

    @Test
    void shouldGetOwnSalarySlips() throws Exception {
        salarySlipRepository.save(SalarySlip.builder()
                .employee(testEmployee)
                .payPeriod(YearMonth.of(2025, 9))
                .basicSalary(new BigDecimal("5000.00"))
                .deductions(new BigDecimal("500.00"))
                .netPay(new BigDecimal("4500.00"))
                .build());

        mockMvc.perform(get("/api/employees/me/salary-slips")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].payPeriod").value("2025-09"));
    }
}
