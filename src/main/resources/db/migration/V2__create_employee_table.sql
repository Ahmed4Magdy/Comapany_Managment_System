CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    position VARCHAR(100) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
     employee_role VARCHAR(50) NOT NULL,
    department_id BIGINT,
    hire_date DATE,
    CONSTRAINT fk_employee_department
    FOREIGN KEY (department_id)
    REFERENCES departments(id)
);
