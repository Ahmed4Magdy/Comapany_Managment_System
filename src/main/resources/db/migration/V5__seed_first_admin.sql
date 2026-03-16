INSERT INTO employees (full_name, email, password, position, employee_role, department_id,hire_date)
SELECT 'System Admin', 'admin@company.com', '$2a$11$Ax2JjfqBFcSPn01Iv/OgM.5uDHTmJuGu0byLpdE4wRrJsda2dnJLm','System Admin', 'ROLE_ADMIN', NULL,CURRENT_DATE()
WHERE NOT EXISTS (SELECT 1 FROM employees WHERE employee_role = 'ROLE_ADMIN');