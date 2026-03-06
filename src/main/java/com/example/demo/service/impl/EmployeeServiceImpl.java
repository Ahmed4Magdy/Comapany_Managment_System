package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Task;
import com.example.demo.exceptionhandling.DepartmentNotFoundException;
import com.example.demo.exceptionhandling.DuplicateEmailException;
import com.example.demo.exceptionhandling.EmployeeNotFoundException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto addEmployee(EmployeeDto dto) {

        Employee employee = employeeMapper.toEntity(dto);
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
        employee.setPassword(dto.getPassword());
        Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new DepartmentNotFoundException("Not Found Department" + dto.getDepartmentId()));
        employee.setDepartment(dept);
        employee.setHireDate(LocalDate.now());
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDto(saved);
    }

    @Override
    public EmployeeDto getEmployeeWithId(Long id) {

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Not Found Employee " + id));
        return employeeMapper.toDto(employee);

    }

    @Override
    public List<EmployeeDto> getAllEmployees() {

        return employeeRepository.findAll().stream().map(employeeMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Not Found Employee " + id));
        Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new DepartmentNotFoundException("Not Found Department " + dto.getDepartmentId()));
        employeeMapper.updateEmployeeFromDto(dto, employee);
        employee.setDepartment(department);
        employee.setHireDate(LocalDate.now());
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toDto(updated);


    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Not Found Employee " + id));

        boolean hasTasks = taskRepository.existsByEmployeeId(id);
        if (hasTasks) {
            throw new RuntimeException("can't delete employee if belong to it ");
        }

        employeeRepository.deleteById(id);

    }

    @Override
    public List<EmployeeDto> getAllActiveTrueEmployees() {

        return employeeRepository.findByActiveTrue().stream().map(employeeMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getAllActiveFalseEmployees() {
        return employeeRepository.findByActiveFalse().stream().map(employeeMapper::toDto).collect(Collectors.toList());
    }


}
