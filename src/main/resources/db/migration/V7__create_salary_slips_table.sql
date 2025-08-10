CREATE TABLE salary_slips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    pay_period VARCHAR(7) NOT NULL,
    basic_salary DECIMAL(19, 2) NOT NULL,
    deductions DECIMAL(19, 2),
    net_pay DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);
