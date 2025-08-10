package com.example.hrms.mapper;

import com.example.hrms.dto.EmployeeDto;
import com.example.hrms.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.role.name", target = "role")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.title", target = "positionTitle")
    EmployeeDto toDto(Employee employee);
}
