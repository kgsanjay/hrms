package com.example.hrms.mapper;

import com.example.hrms.dto.LeaveRequestDto;
import com.example.hrms.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {
    @Mapping(source = "employee.id", target = "employeeId")
    LeaveRequestDto toDto(LeaveRequest leaveRequest);
}
