package com.example.hrms.controller;

import com.example.hrms.entity.*;
import com.example.hrms.repository.*;
import com.example.hrms.service.StorageService;
import com.example.hrms.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DocumentControllerTest {

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
    private DocumentRepository documentRepository;

    @Autowired
    private com.example.hrms.repository.SalarySlipRepository salarySlipRepository;

    @Autowired
    private com.example.hrms.repository.AttendanceRepository attendanceRepository;

    @Autowired
    private com.example.hrms.repository.LeaveRequestRepository leaveRequestRepository;

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

    @Autowired
    private StorageService storageService;

    private String adminToken;
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
        storageService.deleteAll();
        storageService.init();

        Role adminRole = roleRepository.save(Role.builder().name(RoleType.ADMIN).build());
        User adminUser = User.builder().username("admin").password(passwordEncoder.encode("password")).role(adminRole).status(UserStatus.ACTIVE).build();
        userRepository.save(adminUser);
        adminToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin"));

        Department department = departmentRepository.save(new Department(null, "IT", null));
        Position position = positionRepository.save(new Position(null, "Developer", department));
        Role employeeRole = roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());
        User employeeUser = User.builder().username("employee").password(passwordEncoder.encode("password")).role(employeeRole).status(UserStatus.ACTIVE).build();
        userRepository.save(employeeUser);
        testEmployee = employeeRepository.save(Employee.builder()
                .user(employeeUser)
                .name("Test Employee")
                .department(department)
                .position(position)
                .joinDate(java.time.LocalDate.now())
                .build());
    }

    @Test
    void shouldUploadDocumentWhenUserIsAdmin() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart("/api/employees/{employeeId}/documents", testEmployee.getId())
                        .file(file)
                        .param("type", "RESUME")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}
