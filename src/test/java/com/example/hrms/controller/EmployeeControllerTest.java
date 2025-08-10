package com.example.hrms.controller;

import com.example.hrms.dto.CreateEmployeeRequest;
import com.example.hrms.entity.*;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.PositionRepository;
import com.example.hrms.repository.RoleRepository;
import com.example.hrms.repository.UserRepository;
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

import java.time.LocalDate;

import com.example.hrms.dto.UpdateProfileRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

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
    private com.example.hrms.repository.SalarySlipRepository salarySlipRepository;

    @Autowired
    private com.example.hrms.repository.DocumentRepository documentRepository;

    @Autowired
    private com.example.hrms.repository.AttendanceRepository attendanceRepository;

    @Autowired
    private com.example.hrms.repository.LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private String adminToken;
    private Department testDepartment;
    private Position testPosition;
    private Role employeeRole;


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
        employeeRole = roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());

        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .role(adminRole)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(adminUser);

        adminToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin"));

        testDepartment = departmentRepository.save(new Department(null, "IT", "Information Technology"));
        testPosition = positionRepository.save(new Position(null, "Software Engineer", testDepartment));
    }

    @Test
    void shouldCreateEmployeeWhenUserIsAdmin() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "John Doe",
                LocalDate.now(),
                "johndoe",
                "password",
                "EMPLOYEE",
                testDepartment.getId(),
                testPosition.getId()
        );

        mockMvc.perform(post("/api/employees")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void shouldUpdateOwnProfile() throws Exception {
        User employeeUser = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("password"))
                .role(employeeRole)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(employeeUser);

        Employee employee = Employee.builder()
                .user(employeeUser)
                .name("Test Employee")
                .department(testDepartment)
                .position(testPosition)
                .joinDate(LocalDate.now())
                .build();
        employeeRepository.save(employee);

        String employeeToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("employee"));

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setPhoneNumber("1234567890");
        request.setAddress("123 Main St");

        mockMvc.perform(put("/api/employees/me")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }
}
