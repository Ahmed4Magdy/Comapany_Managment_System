package com.example.demo.Service;


import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.exceptionhandling.DepartmentNotFoundException;
import com.example.demo.exceptionhandling.DuplicateEmailException;
import com.example.demo.exceptionhandling.EmployeeNotFoundException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee employee;
    private Employee employee1;
    private EmployeeDto dto;
    private Department dept;


    @BeforeEach
    void setup() {

        dto = new EmployeeDto();
        dto.setDepartmentId(1L);
        dto.setFullName("Ahmed Magdy");
        dto.setEmail("ahmed@gmail.com");
        dto.setPassword("hwdkh@22");
        dto.setPosition("Java developer");

        employee = new Employee();
        employee.setFullName("Ahmed Magdy");
        employee.setEmail("ahmed@gmail.com");
        employee.setPassword("hwdkh@22");
        employee.setPosition("Java developer");
        employee.setHireDate(LocalDate.now());

    }

    @Test
    void addEmployee_shouldReturnSuccessfully() {

        dept = new Department();
        dept.setName("HR");


        when(employeeMapper.toEntity(dto)).thenReturn(employee);
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.of(dept));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(dto);


        EmployeeDto result = employeeServiceImpl.addEmployee(dto);
        assertNotNull(result);
        assertEquals("Ahmed Magdy", result.getFullName());
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void addEmployee_givenDuplicateEmail_shouldThrowDuplicateEmailException() {

        when(employeeMapper.toEntity(dto)).thenReturn(employee);
        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        Exception ex = assertThrows(DuplicateEmailException.class, () -> {
            employeeServiceImpl.addEmployee(dto);
        });

        assertEquals("Email already exists", ex.getMessage());

    }

    @Test
    void addEmployee_givenDepartmentNotFound_addEmployee_givenDuplicateEmail_shouldThrowDepartmentNotFoundException() {

        when(employeeMapper.toEntity(dto)).thenReturn(employee);
        when(departmentRepository.findById(dto.getDepartmentId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(DepartmentNotFoundException.class, () -> {
            employeeServiceImpl.addEmployee(dto);
        });

        assertEquals("Not Found Department" + 1, ex.getMessage());
        verify(employeeRepository, never()).save(any());
    }




    @Test
    void getEmployeeWithId_shouldReturnEmployeeWithSuccessfully() {


        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        EmployeeDto result = employeeServiceImpl.getEmployeeWithId(anyLong());

        assertEquals("Ahmed Magdy", result.getFullName());
        verify(employeeRepository, times(1)).findById(anyLong());
    }


    @Test
    void getEmployeeWithId_shouldThrowNotFoundEmployeeException() {


        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeServiceImpl.getEmployeeWithId(1L);
        });
        assertEquals("Not Found Employee " + 1L, ex.getMessage());
        verify(employeeMapper, never()).toDto(any());
    }


    @Test
    void getAllEmployees_ShouldReturnAllEmployeesWithSuccessfully() {
        employee1 = new Employee();
        employee1.setFullName("Yassin ayamn");
        employee1.setEmail("yasso@gmail.com");
        employee1.setPassword("hwdkh@22");
        employee1.setPosition("Java developer");
        employee1.setHireDate(LocalDate.now());


        when(employeeRepository.findAll()).thenReturn(List.of(employee, employee1));
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        List<EmployeeDto> result = employeeServiceImpl.getAllEmployees();
        assertNotNull(result);
        assertEquals(2, result.size());


    }


    @Test
    void updateEmployee_shouldReturnUpdatedEmployee() {
        dept = new Department();
        dept.setName("HR");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        doNothing().when(employeeMapper).updateEmployeeFromDto(dto, employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(dto);
        EmployeeDto result = employeeServiceImpl.updateEmployee(1L, dto);

        assertNotNull(result);
        assertEquals(dto, result);

    }

    @Test
    void updateEmployee_shouldReturnEmployeeNotFoundException() {


        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeServiceImpl.updateEmployee(1L, dto);
        });

        assertEquals("Not Found Employee " + 1, ex.getMessage());
        verify(employeeMapper, never()).updateEmployeeFromDto(any(), any());

    }


    @Test
    void deleteEmployee_shouldDeletedEmployeeWithSuccessfully() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.existsByEmployeeId(1L)).thenReturn(false);
        doNothing().when(employeeRepository).deleteById(1L);
        employeeServiceImpl.deleteEmployee(1L);
        verify(employeeRepository,times(1)).deleteById(1L);


    }

    @Test
    void deleteEmployee_shouldThrowRuntimeException_CannotDeleteEmployeeIfBelongToDepartment(){

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(taskRepository.existsByEmployeeId(1L)).thenReturn(true);

         Exception ex =assertThrows(RuntimeException.class,()->{
             employeeServiceImpl.deleteEmployee(1L);
         });

        verify(employeeRepository,never()).deleteById(anyLong());


    }


    @Test
    void deleteEmployee_shouldThrowEmployeeNotFoundException(){


        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());


        Exception ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeServiceImpl.deleteEmployee(1L);
        });

        assertEquals("Not Found Employee " + 1, ex.getMessage());

    }


    @Test
    void getActiveTrueEmployees(){

        when(employeeRepository.findByActiveTrue()).thenReturn(List.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        List <EmployeeDto>result =employeeServiceImpl.getAllActiveTrueEmployees();

        assertEquals(1,result.size());
    }


    @Test
    void getActiveFalseEmployees(){

        when(employeeRepository.findByActiveFalse()).thenReturn(List.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        List <EmployeeDto>result =employeeServiceImpl.getAllActiveFalseEmployees();

        assertEquals(1,result.size());
    }


}
