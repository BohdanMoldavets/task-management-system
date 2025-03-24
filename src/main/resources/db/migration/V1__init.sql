CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP default CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    UNIQUE (username)
);

INSERT INTO employees(id, created, email, password, username) values ('1','2025-03-19 14:37:44.146000','anna@gmail.com','$2a$10$I1kvGoZ4WfwJxmOvacSJveWBRUFg2sZTCtqe2w2dGOp04EmcC0qma','Anna');

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP default CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT NULL,
    name VARCHAR(255) NOT NULL
);

INSERT INTO roles (name) VALUES ('ROLE_EMPLOYEE'), ('ROLE_MANAGER');

CREATE TABLE employees_roles (
    employee_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE (employee_id, role_id),
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

INSERT INTO employees_roles (employee_id, role_id) VALUES (1, 2);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP default CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE employees_tasks (
    employee_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    UNIQUE (employee_id, task_id),
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

