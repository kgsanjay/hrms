package com.example.hrms.mapper;

import com.example.hrms.dto.TrainingProgramDto;
import com.example.hrms.entity.TrainingProgram;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {
    TrainingProgramDto toDto(TrainingProgram trainingProgram);
}
