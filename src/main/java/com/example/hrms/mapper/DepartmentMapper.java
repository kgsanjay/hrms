package com.example.hrms.mapper;

import com.example.hrms.dto.DepartmentDto;
import com.example.hrms.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDto toDto(Department department);
    Department toEntity(DepartmentDto departmentDto);
}
