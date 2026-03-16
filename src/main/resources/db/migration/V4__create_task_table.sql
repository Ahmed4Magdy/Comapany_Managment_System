CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    deadline DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    employee_id BIGINT,
    project_id BIGINT,


    CONSTRAINT fk_task_employee
         FOREIGN KEY (employee_id)
         REFERENCES employees(id)
         ON DELETE SET NULL,


    CONSTRAINT fk_task_project
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE
);
/*
If an employee is deleted from employees, the employee_id in the related tasks records will automatically become NULL.
If a project is deleted from projects, the project_id in the related tasks records will automatically be deleted.
*/