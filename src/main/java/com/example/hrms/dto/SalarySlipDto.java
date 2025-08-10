package com.example.hrms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class SalarySlipDto {
    private Long id;
    private Long employeeId;
    private YearMonth payPeriod;
    private BigDecimal basicSalary;
    private BigDecimal deductions;
    private BigDecimal netPay;
}
