package com.example.hrms.controller;

import com.example.hrms.dto.CreateUpdateHolidayRequest;
import com.example.hrms.entity.*;
import com.example.hrms.repository.HolidayRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HolidayRepository holidayRepository;

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
        holidayRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role adminRole = roleRepository.save(Role.builder().name(RoleType.ADMIN).build());
        User adminUser = User.builder().username("admin").password(passwordEncoder.encode("password")).role(adminRole).status(UserStatus.ACTIVE).build();
        userRepository.save(adminUser);
        adminToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("admin"));

        Role employeeRole = roleRepository.save(Role.builder().name(RoleType.EMPLOYEE).build());
        User employeeUser = User.builder().username("employee").password(passwordEncoder.encode("password")).role(employeeRole).status(UserStatus.ACTIVE).build();
        userRepository.save(employeeUser);
        employeeToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername("employee"));
    }

    @Test
    void shouldCreateHolidayWhenUserIsAdmin() throws Exception {
        CreateUpdateHolidayRequest request = new CreateUpdateHolidayRequest(LocalDate.of(2025, 1, 1), "New Year");

        mockMvc.perform(post("/api/holidays")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Year"));
    }

    @Test
    void shouldNotCreateHolidayWhenUserIsEmployee() throws Exception {
        CreateUpdateHolidayRequest request = new CreateUpdateHolidayRequest(LocalDate.of(2025, 1, 1), "New Year");

        mockMvc.perform(post("/api/holidays")
                        .header("Authorization", "Bearer " + employeeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
