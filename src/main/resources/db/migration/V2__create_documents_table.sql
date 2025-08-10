CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    document_type VARCHAR(20) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    upload_timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_documents_employees FOREIGN KEY (employee_id) REFERENCES employees(id)
);
