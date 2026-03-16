package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto addEmployee(EmployeeDto dto);

    AuthResponse login(AuthRequest authRequest);

    EmployeeDto getEmployeeWithId(Long id);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(Long id, EmployeeDto dto);

    void deleteEmployee(Long id);

    List<EmployeeDto> getAllActiveTrueEmployees();

    List<EmployeeDto> getAllActiveFalseEmployees();


}
