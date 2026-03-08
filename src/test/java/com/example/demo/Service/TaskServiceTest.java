package com.example.demo.Service;

import com.example.demo.dto.TaskDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.exceptionhandling.EmployeeNotFoundException;
import com.example.demo.exceptionhandling.ProjectNotFoundException;
import com.example.demo.exceptionhandling.TaskNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.impl.TaskServiceImpl;
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
public class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceimpl;

    private TaskDto dto;
    private Task task;
    private Employee employee;
    private Project project;

    @BeforeEach
    void setup() {

        dto = new TaskDto();
        dto.setTitle("Design UI");
        dto.setDescription("Homepage design");
        dto.setDeadline(LocalDate.now());
        dto.setEmployeeId(1L);
        dto.setProjectId(1L);

        task = new Task();
        task.setTitle("Design UI");
        task.setTitle("Design UI");
        task.setDescription("Homepage design");
        task.setDeadline(LocalDate.now());


    }

    @Test
    void addTask_ShouldReturnTaskSuccessfully() {
        employee = new Employee();
        employee.setFullName("Ahmed Magdy");
        employee.setEmail("ahmed@gmail.com");
        employee.setPassword("hwdkh@22");
        employee.setPosition("Java developer");
        employee.setHireDate(LocalDate.now());

        project = new Project();
        project.setName("Web application");
        project.setStartDate(LocalDate.of(2026, 02, 28));
        project.setEndDate(LocalDate.of(2026, 03, 28));


        when(employeeRepository.findById(dto.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(project));
        when(taskMapper.toEntity(dto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(dto);


        TaskDto result = taskServiceimpl.addTask(dto);
        assertNotNull(result);
        assertEquals("Design UI", result.getTitle());
        verify(taskRepository, times(1)).save(any());

    }


    @Test
    void addTask_EmployeeIsNoActive_shouldReturnRuntimeException() {
        employee = new Employee();
        employee.setFullName("Ahmed Magdy");
        employee.setEmail("ahmed@gmail.com");
        employee.setPassword("hwdkh@22");
        employee.setPosition("Java developer");
        employee.setHireDate(LocalDate.now());
        employee.setActive(false);

        when(employeeRepository.findById(dto.getEmployeeId())).thenReturn(Optional.of(employee));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            taskServiceimpl.addTask(dto);
        });

        assertEquals("can't create task have inactive employee ", ex.getMessage());
    }


    @Test
    void addTask_EmployeeNotFound_shouldReturnNotFoundException() {
        employee = new Employee();
        employee.setFullName("Ahmed Magdy");
        employee.setEmail("ahmed@gmail.com");
        employee.setPassword("hwdkh@22");
        employee.setPosition("Java developer");
        employee.setHireDate(LocalDate.now());

        when(employeeRepository.findById(dto.getEmployeeId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(EmployeeNotFoundException.class, () -> {
            taskServiceimpl.addTask(dto);
        });

        assertEquals("Not Found Employee " + 1, ex.getMessage());
    }


    @Test
    void addTask_ProjectNotFound_shouldReturnNotFoundException() {
        employee = new Employee();
        employee.setFullName("Ahmed Magdy");
        employee.setEmail("ahmed@gmail.com");
        employee.setPassword("hwdkh@22");
        employee.setPosition("Java developer");
        employee.setHireDate(LocalDate.now());


        when(employeeRepository.findById(dto.getEmployeeId())).thenReturn(Optional.of(employee));
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ProjectNotFoundException.class, () -> {
            taskServiceimpl.addTask(dto);
        });

        assertEquals("Not Found Project " + 1, ex.getMessage());
    }

    @Test
    void getTaskWithId_shouldReturnTaskWithSuccessfully() {


        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(dto);

        TaskDto result = taskServiceimpl.getTaskWithId(anyLong());

        assertEquals("Design UI", result.getTitle());
        verify(taskRepository, times(1)).findById(anyLong());
    }


    @Test
    void getTaskWithId_shouldThrowNotFoundTaskException() {


        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(TaskNotFoundException.class, () -> {
            taskServiceimpl.getTaskWithId(1L);
        });
        assertEquals("Not Found task " + 1L, ex.getMessage());
        verify(taskMapper, never()).toDto(any());
    }


    @Test
    void getAllTasks_ShouldReturnAllTasksWithSuccessfully() {

        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(dto);

        List<TaskDto> result = taskServiceimpl.getAllTasks();
        assertNotNull(result);
        assertEquals(1, result.size());


    }


    @Test
    void updateTask_shouldReturnUpdatedTask() {

        employee = new Employee();
        employee.setFullName("Ahmed Magdy");
        employee.setEmail("ahmed@gmail.com");
        employee.setPassword("hwdkh@22");
        employee.setPosition("Java developer");
        employee.setHireDate(LocalDate.now());

        project = new Project();
        project.setName("Web application");
        project.setStartDate(LocalDate.of(2026, 02, 28));
        project.setEndDate(LocalDate.of(2026, 03, 28));


        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(taskMapper).updateTaskFromdto(dto, task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(dto);
        TaskDto result = taskServiceimpl.updateTasks(1L, dto);

        assertNotNull(result);
        assertEquals(dto, result);

    }

    @Test
    void updateTask_shouldReturnTaskNotFoundException() {


        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(TaskNotFoundException.class, () -> {
            taskServiceimpl.updateTasks(1L, dto);
        });

        assertEquals("Not Found task " + 1, ex.getMessage());
        verify(taskMapper, never()).updateTaskFromdto(any(), any());

    }


    @Test
    void deleteTask_shouldDeletedTaskWithSuccessfully() {
        doNothing().when(taskRepository).deleteById(1L);
        taskServiceimpl.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);


    }


    @Test
    void getAllTasksWithEmployeeId() {

        when(taskRepository.findByEmployeeId(1L)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(dto);

        List<TaskDto> result = taskServiceimpl.getAllTasksWithEmployeeId(1L);

        assertEquals(1, result.size());


    }

    @Test
    void getAllTasksWithProjectId() {

        when(taskRepository.findByProjectId(1L)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(dto);

        List<TaskDto> result = taskServiceimpl.getAllTasksWithProjectId(1L);

        assertEquals(1, result.size());


    }



}
