package com.example.hrms.controller;

import com.example.hrms.dto.CreateLeaveRequest;
import com.example.hrms.dto.UpdateLeaveRequestStatusRequest;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LeaveRequestControllerTest {

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
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private com.example.hrms.repository.SalarySlipRepository salarySlipRepository;

    @Autowired
    private com.example.hrms.repository.AttendanceRepository attendanceRepository;

    @Autowired
    private com.example.hrms.repository.GoalRepository goalRepository;

    @Autowired
    private com.example.hrms.repository.PerformanceReviewRepository performanceReviewRepository;

    @Autowired
    private com.example.hrms.repository.EnrollmentRepository enrollmentRepository;

    @Autowired
    private com.example.hrms.repository.TrainingProgramRepository trainingProgramRepository;

    @Autowired
    private com.example.hrms.repository.OnboardingTaskRepository onboardingTaskRepository;

    @Autowired
    private com.example.hrms.repository.OffboardingTaskRepository offboardingTaskRepository;

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
        enrollmentRepository.deleteAll();
        goalRepository.deleteAll();
        performanceReviewRepository.deleteAll();
        salarySlipRepository.deleteAll();
        documentRepository.deleteAll();
        attendanceRepository.deleteAll();
        leaveRequestRepository.deleteAll();
        onboardingTaskRepository.deleteAll();
        offboardingTaskRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        positionRepository.deleteAll();
        trainingProgramRepository.deleteAll();
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
    void shouldCreateLeaveRequest() throws Exception {
        CreateLeaveRequest request = new CreateLeaveRequest(
                testEmployee.getId(),
                LeaveType.ANNUAL,
                LocalDate.of(2025, 10, 10),
                LocalDate.of(2025, 10, 20)
        );

        mockMvc.perform(post("/api/leave-requests")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldUpdateLeaveRequestStatusWhenUserIsAdmin() throws Exception {
        LeaveRequest leaveRequest = leaveRequestRepository.save(LeaveRequest.builder()
                .employee(testEmployee)
                .leaveType(LeaveType.SICK)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(LeaveRequestStatus.PENDING)
                .build());

        UpdateLeaveRequestStatusRequest request = new UpdateLeaveRequestStatusRequest(LeaveRequestStatus.APPROVED);

        mockMvc.perform(patch("/api/leave-requests/{id}/status", leaveRequest.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
