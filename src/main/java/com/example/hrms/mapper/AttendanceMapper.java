package com.example.hrms.mapper;

import com.example.hrms.dto.AttendanceDto;
import com.example.hrms.dto.CreateAttendanceRequest;
import com.example.hrms.entity.Attendance;
import com.example.hrms.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.user.username", target = "employeeName")
    @Mapping(source = "checkIn", target = "checkInTime")
    @Mapping(source = "checkOut", target = "checkOutTime")
    AttendanceDto toDto(Attendance attendance);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "checkIn", source = "request.checkInTime")
    @Mapping(target = "checkOut", ignore = true)
    Attendance toEntity(CreateAttendanceRequest request, Employee employee);
}
