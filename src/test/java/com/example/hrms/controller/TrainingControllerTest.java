package com.example.hrms.controller;

import com.example.hrms.dto.CreateEnrollmentRequest;
import com.example.hrms.dto.CreateTrainingProgramRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TrainingControllerTest {

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
    private TrainingProgramRepository trainingProgramRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private SalarySlipRepository salarySlipRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;
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
    private TrainingProgram testTrainingProgram;

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
        trainingProgramRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        positionRepository.deleteAll();
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

        testTrainingProgram = trainingProgramRepository.save(TrainingProgram.builder().title("Java Training").description("Advanced Java Training").maxCapacity(20).build());
    }

    @Test
    void shouldCreateTrainingProgramWhenUserIsAdmin() throws Exception {
        CreateTrainingProgramRequest request = new CreateTrainingProgramRequest();
        request.setTitle("Python Training");
        request.setDescription("Advanced Python Training");

        mockMvc.perform(post("/api/training-programs")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Python Training"));
    }

    @Test
    void shouldCreateEnrollmentWhenUserIsAdmin() throws Exception {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest();
        request.setEmployeeId(testEmployee.getId());
        request.setTrainingProgramId(testTrainingProgram.getId());

        mockMvc.perform(post("/api/enrollments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(testEmployee.getId()));
    }
}
