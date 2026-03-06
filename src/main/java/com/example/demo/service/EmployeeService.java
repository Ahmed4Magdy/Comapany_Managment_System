package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto addEmployee(EmployeeDto dto);

    EmployeeDto getEmployeeWithId(Long id);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(Long id,EmployeeDto dto);

    void deleteEmployee(Long id);


    List<EmployeeDto> getAllActiveTrueEmployees();
    List<EmployeeDto> getAllActiveFalseEmployees();


}
