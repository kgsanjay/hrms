package com.example.hrms.mapper;

import com.example.hrms.dto.GoalDto;
import com.example.hrms.entity.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    GoalDto toDto(Goal goal);
}
