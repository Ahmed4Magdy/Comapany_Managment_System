package com.example.demo.Service;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import com.example.demo.exceptionhandling.DepartmentNotFoundException;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentMapper departmentMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentServiceimpl;


    private Department department;
    private DepartmentDto dto;


    @Test
    void addDepartment_shouldReturnSuccessfully() {

        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");

        when(departmentMapper.toEntity(dto)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toDto(department)).thenReturn(dto);

        DepartmentDto result = departmentServiceimpl.createDepartment(dto);

        assertEquals("HR", result.getName());
        verify(departmentMapper, times(1)).toEntity(any());
    }


    @Test
    void getDepartmentWithId_shouldReturnDepartmentWithSuccessfully() {

        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department)).thenReturn(dto);


        DepartmentDto result = departmentServiceimpl.getDepartmentWithId(anyLong());
        assertNotNull(result);
        assertEquals("HR", result.getName());


    }


    @Test
    void getDepartmentWithId_shouldReturnDepartmentNotFoundException() {

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(DepartmentNotFoundException.class, () -> {
            departmentServiceimpl.getDepartmentWithId(1L);
        });


        verify(departmentMapper, never()).toDto(any());


    }

    @Test
    void getAllDepartments_ShouldReturnAllDepartmentsWithSuccessfully() {

        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");

        when(departmentRepository.findAll()).thenReturn(List.of(department));
        when(departmentMapper.toDto(department)).thenReturn(dto);

        List<DepartmentDto> departments = departmentServiceimpl.getAllDepartments();

        assertNotNull(departments);
        assertEquals(1, departments.size());


    }

    @Test
    void updateDepartment_shouldReturnUpdatedDepartment() {
        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");


        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        doNothing().when(departmentMapper).updateDepartmentFromDto(dto, department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toDto(department)).thenReturn(dto);

        DepartmentDto result = departmentServiceimpl.updateDepartment(1L, dto);
        assertNotNull(result);
        assertEquals(dto, result);


    }

    @Test
    void updateDepartment_shouldReturnDepartmentNotFoundException() {

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(DepartmentNotFoundException.class, () -> {
            departmentServiceimpl.updateDepartment(1L, dto);
        });

        assertEquals("Not Found Department " + 1, ex.getMessage());
        verify(departmentMapper, never()).updateDepartmentFromDto(any(), any());


    }

    @Test
    void deleteDepartment_shouldDeletedEmployeeWithSuccessfully() {
        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(false);
        doNothing().when(departmentRepository).deleteById(1L);
        departmentServiceimpl.deleteDepartment(1L);
        verify(departmentRepository, times(1)).deleteById(1L);


    }

    @Test
    void deleteDepartment_shouldThrowRuntimeException_CannotDeleteDepartmentIfBelongToEmployee() {

        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(true);

        Exception ex = assertThrows(RuntimeException.class, () -> {
            departmentServiceimpl.deleteDepartment(1L);
        });

        assertEquals("Cannot delete department if employees belong to it", ex.getMessage());

    }


    @Test
    void deleteDepartment_shouldThrowDepartmentNotFoundException(){

        dto = new DepartmentDto();
        dto.setName("HR");
        department = new Department();
        department.setName("HR");

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());


        Exception ex = assertThrows(DepartmentNotFoundException.class, () -> {
            departmentServiceimpl.deleteDepartment(1L);
        });

        assertEquals("Not Found Department " + 1, ex.getMessage());

    }


}