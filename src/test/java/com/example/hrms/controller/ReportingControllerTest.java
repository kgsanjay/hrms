package com.example.hrms.controller;

import com.example.hrms.entity.*;
import com.example.hrms.repository.*;
import com.example.hrms.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReportingControllerTest {

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
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private SalarySlipRepository salarySlipRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private String adminToken;
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
        User adminUser = User.builder().username("admin").password(passwordEncoder.encode("password")).role(adminRole).status(UserStatus.ACTIVE).build();
        userRepository.save(adminUser);
        adminToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin"));

        testDepartment = departmentRepository.save(new Department(null, "IT", null));
    }

    @Test
    void shouldGetAttendanceReportWhenUserIsAdmin() throws Exception {
        mockMvc.perform(get("/api/reports/attendance")
                        .param("departmentId", testDepartment.getId().toString())
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetLeaveReportWhenUserIsAdmin() throws Exception {
        mockMvc.perform(get("/api/reports/leave")
                        .param("departmentId", testDepartment.getId().toString())
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}
