package com.example.hrms.repository;

import com.example.hrms.entity.SalarySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalarySlipRepository extends JpaRepository<SalarySlip, Long> {
    List<SalarySlip> findByEmployeeId(Long employeeId);
}
