package com.example.hrms.mapper;

import com.example.hrms.dto.OnboardingTaskDto;
import com.example.hrms.entity.OnboardingTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OnboardingTaskMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    OnboardingTaskDto toDto(OnboardingTask onboardingTask);
}
