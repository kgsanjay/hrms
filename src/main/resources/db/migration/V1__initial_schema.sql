CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE positions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    department_id BIGINT NOT NULL,
    CONSTRAINT fk_positions_departments FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    name VARCHAR(255) NOT NULL,
    department_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    join_date DATE NOT NULL,
    CONSTRAINT fk_employees_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_employees_departments FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_employees_positions FOREIGN KEY (position_id) REFERENCES positions(id)
);
