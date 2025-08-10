package com.example.hrms.mapper;

import com.example.hrms.dto.OffboardingTaskDto;
import com.example.hrms.entity.OffboardingTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OffboardingTaskMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    OffboardingTaskDto toDto(OffboardingTask offboardingTask);
}
