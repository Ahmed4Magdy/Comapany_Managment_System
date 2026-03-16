package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.exceptionhandling.BadCredentialsException;
import com.example.demo.exceptionhandling.DepartmentNotFoundException;
import com.example.demo.exceptionhandling.DuplicateEmailException;
import com.example.demo.exceptionhandling.EmployeeNotFoundException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeDto addEmployee(EmployeeDto dto) {

        Employee employee = employeeMapper.toEntity(dto);

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already exists " + dto.getEmail());
        }
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new DepartmentNotFoundException("Not Found Department" + dto.getDepartmentId()));
        employee.setDepartment(dept);
        employee.setHireDate(LocalDate.now());
        Employee saved = employeeRepository.save(employee);
        dto.setDepartmentName(saved.getDepartment().getName());
        return employeeMapper.toDto(saved);
    }


    @Override
    public AuthResponse login(AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );


        String token = jwtService.generateToken(authRequest.getEmail());
        return new AuthResponse(token, "Login successful");

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
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setDepartment(department);
        employee.setHireDate(LocalDate.now());
        Employee updated = employeeRepository.save(employee);
        dto.setDepartmentName(updated.getDepartment().getName());

        return employeeMapper.toDto(updated);


    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Not Found Employee " + id));


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
