CREATE TABLE goals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    target_date DATE,
    status VARCHAR(20) NOT NULL,
    performance_review_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE TABLE performance_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    review_date DATE NOT NULL,
    comments TEXT,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (reviewer_id) REFERENCES employees(id)
);

ALTER TABLE goals ADD CONSTRAINT fk_goals_performance_review FOREIGN KEY (performance_review_id) REFERENCES performance_reviews(id);
