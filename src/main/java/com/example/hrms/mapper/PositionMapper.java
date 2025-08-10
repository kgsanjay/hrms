package com.example.hrms.mapper;

import com.example.hrms.dto.PositionDto;
import com.example.hrms.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    @Mapping(source = "department.id", target = "departmentId")
    PositionDto toDto(Position position);
}
