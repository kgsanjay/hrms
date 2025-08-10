CREATE TABLE attendances (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_attendances_employees FOREIGN KEY (employee_id) REFERENCES employees(id)
);
