package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "salary_slips")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalarySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private YearMonth payPeriod;

    @Column(nullable = false)
    private BigDecimal basicSalary;

    private BigDecimal deductions;

    @Column(nullable = false)
    private BigDecimal netPay;
}
