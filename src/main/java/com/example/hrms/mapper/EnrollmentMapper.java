package com.example.hrms.mapper;

import com.example.hrms.dto.EnrollmentDto;
import com.example.hrms.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    EnrollmentDto toDto(Enrollment enrollment);
}
