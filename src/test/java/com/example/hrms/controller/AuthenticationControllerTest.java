package com.example.hrms.controller;

import com.example.hrms.dto.AuthRequest;
import com.example.hrms.entity.Role;
import com.example.hrms.entity.RoleType;
import com.example.hrms.entity.User;
import com.example.hrms.entity.UserStatus;
import com.example.hrms.repository.RoleRepository;
import com.example.hrms.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private com.example.hrms.repository.EmployeeRepository employeeRepository;

    @Autowired
    private com.example.hrms.repository.DepartmentRepository departmentRepository;

    @Autowired
    private com.example.hrms.repository.PositionRepository positionRepository;

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
    }

    @Test
    void shouldReturnJwtWhenUserAuthenticatesSuccessfully() throws Exception {
        Role userRole = roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());
        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest("testuser", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isNotEmpty());
    }
}
