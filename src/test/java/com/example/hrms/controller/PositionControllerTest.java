package com.example.hrms.controller;

import com.example.hrms.dto.CreateUpdatePositionRequest;
import com.example.hrms.entity.*;
import com.example.hrms.repository.DepartmentRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PositionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private com.example.hrms.repository.EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

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
    private String employeeToken;
    private Department testDepartment;

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
        Role employeeRole = roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());

        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .role(adminRole)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(adminUser);

        User employeeUser = User.builder()
                .username("employee")
                .password(passwordEncoder.encode("password"))
                .role(employeeRole)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(employeeUser);

        adminToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin"));
        employeeToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("employee"));

        testDepartment = departmentRepository.save(new Department(null, "IT", "Information Technology"));
    }

    @Test
    void shouldCreatePositionWhenUserIsAdmin() throws Exception {
        CreateUpdatePositionRequest request = new CreateUpdatePositionRequest("Software Engineer", testDepartment.getId());

        mockMvc.perform(post("/api/positions")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void shouldNotCreatePositionWhenUserIsEmployee() throws Exception {
        CreateUpdatePositionRequest request = new CreateUpdatePositionRequest("Software Engineer", testDepartment.getId());

        mockMvc.perform(post("/api/positions")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetPositionById() throws Exception {
        Position position = positionRepository.save(new Position(null, "Software Engineer", testDepartment));

        mockMvc.perform(get("/api/positions/" + position.getId())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void shouldGetAllPositionsByDepartment() throws Exception {
        positionRepository.save(new Position(null, "Software Engineer", testDepartment));
        positionRepository.save(new Position(null, "Senior Software Engineer", testDepartment));

        mockMvc.perform(get("/api/positions").param("departmentId", testDepartment.getId().toString())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}
