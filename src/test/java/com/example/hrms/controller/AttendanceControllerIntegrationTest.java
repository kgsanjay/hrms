package com.example.hrms.controller;

import com.example.hrms.dto.CreateAttendanceRequest;
import com.example.hrms.dto.UpdateAttendanceRequest;
import com.example.hrms.entity.Attendance;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.User;
import com.example.hrms.entity.enums.AttendanceStatus;
import com.example.hrms.entity.Department;
import com.example.hrms.entity.Position;
import com.example.hrms.entity.Role;
import com.example.hrms.entity.UserStatus;
import com.example.hrms.repository.AttendanceRepository;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.PositionRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AttendanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private com.example.hrms.repository.RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    private User employeeUser;
    private Employee employee;

    @BeforeEach
    void setUp() {
        attendanceRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role employeeRole = new Role();
        employeeRole.setName(com.example.hrms.entity.RoleType.EMPLOYEE);
        roleRepository.save(employeeRole);

        employeeUser = new User();
        employeeUser.setUsername("employee");
        employeeUser.setPassword("password");
        employeeUser.setRole(employeeRole);
        employeeUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(employeeUser);

        Department department = new Department();
        department.setName("IT");
        department.setDescription("IT Department");
        departmentRepository.save(department);

        Position position = new Position();
        position.setTitle("Software Engineer");
        position.setDepartment(department);
        positionRepository.save(position);

        employee = new Employee();
        employee.setUser(employeeUser);
        employee.setName("Test Employee");
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setJoinDate(LocalDate.now());
        employeeRepository.save(employee);
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void testClockIn_Success() throws Exception {
        CreateAttendanceRequest request = new CreateAttendanceRequest();
        request.setEmployeeId(employee.getId());
        request.setDate(LocalDate.now());
        request.setCheckInTime(LocalTime.of(9, 0));

        mockMvc.perform(post("/api/attendances/clock-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(employee.getId()))
                .andExpect(jsonPath("$.status").value("PRESENT"));
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void testClockOut_Success() throws Exception {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.of(9, 0));
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendanceRepository.save(attendance);

        mockMvc.perform(post("/api/attendances/clock-out/" + employee.getId() + "?date=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employee.getId()))
                .andExpect(jsonPath("$.checkOutTime").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetEmployeeAttendance_AsAdmin() throws Exception {
        // ... (implementation for get attendance)
        mockMvc.perform(get("/api/attendances/employee/" + employee.getId() + "?startDate=" + LocalDate.now().minusDays(1) + "&endDate=" + LocalDate.now()))
                 .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateAttendance_AsAdmin() throws Exception {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.of(9, 0));
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance = attendanceRepository.save(attendance);

        UpdateAttendanceRequest request = new UpdateAttendanceRequest();
        request.setStatus(AttendanceStatus.ON_LEAVE);

        mockMvc.perform(put("/api/attendances/" + attendance.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(attendance.getId()))
                .andExpect(jsonPath("$.status").value("ON_LEAVE"));
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void testUpdateAttendance_AsEmployee_Forbidden() throws Exception {
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.of(9, 0));
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance = attendanceRepository.save(attendance);

        UpdateAttendanceRequest request = new UpdateAttendanceRequest();
        request.setStatus(AttendanceStatus.ON_LEAVE);

        mockMvc.perform(put("/api/attendances/" + attendance.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
