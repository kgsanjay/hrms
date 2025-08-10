package com.example.hrms.mapper;

import com.example.hrms.dto.PerformanceReviewDto;
import com.example.hrms.entity.PerformanceReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = GoalMapper.class)
public interface PerformanceReviewMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "reviewer.id", target = "reviewerId")
    PerformanceReviewDto toDto(PerformanceReview performanceReview);
}
