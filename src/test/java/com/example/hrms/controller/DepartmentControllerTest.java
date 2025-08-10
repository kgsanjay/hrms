package com.example.hrms.controller;

import com.example.hrms.dto.CreateUpdateDepartmentRequest;
import com.example.hrms.entity.Department;
import com.example.hrms.entity.Role;
import com.example.hrms.entity.RoleType;
import com.example.hrms.entity.User;
import com.example.hrms.entity.UserStatus;
import com.example.hrms.repository.DepartmentRepository;
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
public class DepartmentControllerTest {

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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private String adminToken;
    private String employeeToken;

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
    }

    @Test
    void shouldCreateDepartmentWhenUserIsAdmin() throws Exception {
        CreateUpdateDepartmentRequest request = new CreateUpdateDepartmentRequest("IT", "Information Technology");

        mockMvc.perform(post("/api/departments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void shouldNotCreateDepartmentWhenUserIsEmployee() throws Exception {
        CreateUpdateDepartmentRequest request = new CreateUpdateDepartmentRequest("IT", "Information Technology");

        mockMvc.perform(post("/api/departments")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetDepartmentById() throws Exception {
        Department department = departmentRepository.save(new Department(null, "HR", "Human Resources"));

        mockMvc.perform(get("/api/departments/" + department.getId())
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void shouldGetAllDepartments() throws Exception {
        departmentRepository.save(new Department(null, "HR", "Human Resources"));
        departmentRepository.save(new Department(null, "IT", "Information Technology"));

        mockMvc.perform(get("/api/departments")
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldUpdateDepartmentWhenUserIsAdmin() throws Exception {
        Department department = departmentRepository.save(new Department(null, "HR", "Human Resources"));
        CreateUpdateDepartmentRequest request = new CreateUpdateDepartmentRequest("Human Resources", "HR Department");

        mockMvc.perform(put("/api/departments/" + department.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Human Resources"));
    }

    @Test
    void shouldDeleteDepartmentWhenUserIsAdmin() throws Exception {
        Department department = departmentRepository.save(new Department(null, "HR", "Human Resources"));

        mockMvc.perform(delete("/api/departments/" + department.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
