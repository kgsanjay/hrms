package com.example.hrms.controller;

import com.example.hrms.dto.CreateGoalRequest;
import com.example.hrms.dto.CreatePerformanceReviewRequest;
import com.example.hrms.dto.UpdateGoalRequest;
import com.example.hrms.entity.*;
import com.example.hrms.entity.enums.GoalStatus;
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
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PerformanceControllerTest {

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
    private GoalRepository goalRepository;
    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;
    @Autowired
    private SalarySlipRepository salarySlipRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private TrainingProgramRepository trainingProgramRepository;
    @Autowired
    private OnboardingTaskRepository onboardingTaskRepository;
    @Autowired
    private OffboardingTaskRepository offboardingTaskRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    private String adminToken;
    private Employee testEmployee;
    private Employee testReviewer;

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

        Department department = departmentRepository.save(new Department(null, "IT", null));
        Position position = positionRepository.save(new Position(null, "Developer", department));

        User employeeUser = User.builder().username("employee").password(passwordEncoder.encode("password")).role(adminRole).status(UserStatus.ACTIVE).build();
        userRepository.save(employeeUser);
        testEmployee = employeeRepository.save(Employee.builder().user(employeeUser).name("Test Employee").department(department).position(position).joinDate(LocalDate.now()).build());

        User reviewerUser = User.builder().username("reviewer").password(passwordEncoder.encode("password")).role(adminRole).status(UserStatus.ACTIVE).build();
        userRepository.save(reviewerUser);
        testReviewer = employeeRepository.save(Employee.builder().user(reviewerUser).name("Test Reviewer").department(department).position(position).joinDate(LocalDate.now()).build());
    }

    @Test
    void shouldCreateGoalWhenUserIsAdmin() throws Exception {
        CreateGoalRequest request = new CreateGoalRequest();
        request.setEmployeeId(testEmployee.getId());
        request.setDescription("Learn new framework");
        request.setTargetDate(LocalDate.of(2025, 12, 31));

        mockMvc.perform(post("/api/goals")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Learn new framework"));
    }

    @Test
    void shouldUpdateGoalWhenUserIsAdmin() throws Exception {
        Goal goal = goalRepository.save(Goal.builder().employee(testEmployee).description("Old Goal").status(GoalStatus.PENDING).build());
        UpdateGoalRequest request = new UpdateGoalRequest();
        request.setDescription("Updated Goal");
        request.setStatus(GoalStatus.IN_PROGRESS);

        mockMvc.perform(put("/api/goals/" + goal.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Goal"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldCreatePerformanceReviewWhenUserIsAdmin() throws Exception {
        Goal goal = goalRepository.save(Goal.builder().employee(testEmployee).description("Test Goal").status(GoalStatus.PENDING).build());
        CreatePerformanceReviewRequest request = new CreatePerformanceReviewRequest();
        request.setEmployeeId(testEmployee.getId());
        request.setReviewerId(testReviewer.getId());
        request.setReviewDate(LocalDate.now());
        request.setComments("Good performance");
        request.setGoalIds(Collections.singletonList(goal.getId()));

        mockMvc.perform(post("/api/performance-reviews")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comments").value("Good performance"));
    }
}
