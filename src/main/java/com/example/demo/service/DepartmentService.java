package com.example.demo.service;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.EmployeeDto;

import java.util.List;

public interface DepartmentService {

    DepartmentDto createDepartment(DepartmentDto dto);

    DepartmentDto getDepartmentWithId(Long id);

    List<DepartmentDto> getAllDepartments();

    DepartmentDto updateDepartment(Long id,DepartmentDto dto);

    void deleteDepartment(Long id);


}
