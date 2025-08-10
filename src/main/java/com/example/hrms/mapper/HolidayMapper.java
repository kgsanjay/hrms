package com.example.hrms.mapper;

import com.example.hrms.dto.HolidayDto;
import com.example.hrms.entity.Holiday;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HolidayMapper {
    HolidayDto toDto(Holiday holiday);
}
