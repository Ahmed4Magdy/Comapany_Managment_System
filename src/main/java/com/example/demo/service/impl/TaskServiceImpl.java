package com.example.demo.service.impl;

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
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final TaskMapper taskMapper;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public TaskDto addTask(TaskDto dto) {

        Employee existing = employeeRepository.findById(dto.getEmployeeId()).orElseThrow(() -> new EmployeeNotFoundException("Not Found Employee " + dto.getEmployeeId()));
        if (!existing.isActive()) {
            throw new RuntimeException("can't create task have inactive employee ");
        }
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Not Found Project " + dto.getProjectId()));

        Task task = taskMapper.toEntity(dto);
        task.setEmployee(existing);
        task.setProject(project);

        Task saved = taskRepository.save(task);
        return taskMapper.toDto(saved);


    }

    @Override
    public TaskDto getTaskWithId(Long id) {

        Task existing = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Not Found task " + id));

        return taskMapper.toDto(existing);


    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTasks(Long id, TaskDto dto) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Not Found task " + id));
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Not Found Project with " + dto.getProjectId()));
        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow(() -> new EmployeeNotFoundException("Not Found Employee " + dto.getEmployeeId()));

        taskMapper.updateTaskFromdto(dto, task);
        task.setEmployee(employee);
        task.setProject(project);

        Task saved = taskRepository.save(task);

        return taskMapper.toDto(saved);


    }

    @Override
    public void deleteTask(Long id) {

        taskRepository.deleteById(id);

    }

    @Override
    public List<TaskDto> getAllTasksWithEmployeeId(Long employeeId) {


         return taskRepository.findByEmployeeId(employeeId).stream().map(taskMapper::toDto).collect(Collectors.toList());


    }

    @Override
    public List<TaskDto> getAllTasksWithProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream().map(taskMapper::toDto).collect(Collectors.toList());
    }
}
