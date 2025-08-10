package com.example.hrms.mapper;

import com.example.hrms.dto.SalarySlipDto;
import com.example.hrms.entity.SalarySlip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalarySlipMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    SalarySlipDto toDto(SalarySlip salarySlip);
}
