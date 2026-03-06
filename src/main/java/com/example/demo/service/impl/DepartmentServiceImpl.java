package com.example.demo.service.impl;


import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import com.example.demo.exceptionhandling.DepartmentNotFoundException;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDto createDepartment(DepartmentDto dto) {

        Department department = departmentMapper.toEntity(dto);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toDto(saved);

    }

    @Override
    public DepartmentDto getDepartmentWithId(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Not Found Department " + id));
        return departmentMapper.toDto(department);

    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(departmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto dto) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException("Not Found Department " + id));
        departmentMapper.updateDepartmentFromDto(dto, department);
        Department updated = departmentRepository.save(department);
        return departmentMapper.toDto(updated);

    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Not Found Department " + id));

        boolean hasEmployees = employeeRepository.existsByDepartmentId(id);

        if (hasEmployees) {
            throw new RuntimeException("Cannot delete department if employees belong to it");
        }


        departmentRepository.deleteById(id);
    }

}