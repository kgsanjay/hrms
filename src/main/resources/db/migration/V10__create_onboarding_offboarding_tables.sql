CREATE TABLE onboarding_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    task_description VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE TABLE offboarding_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    task_description VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);
