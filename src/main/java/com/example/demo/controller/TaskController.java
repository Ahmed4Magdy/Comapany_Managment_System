package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.exceptionhandling.EmployeeNotFoundException;
import com.example.demo.exceptionhandling.ProjectNotFoundException;
import com.example.demo.exceptionhandling.TaskNotFoundException;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create")
    public ResponseEntity<TaskDto> addTask(@RequestBody @Valid TaskDto dto) {

        TaskDto saved = taskService.addTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskWithId(@PathVariable Long id) {


        return ResponseEntity.ok(taskService.getTaskWithId(id));


    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTasks(@PathVariable Long id, @Valid @RequestBody TaskDto dto) {

        return ResponseEntity.ok(taskService.updateTasks(id, dto));


    }


    @GetMapping("/withEmployee/{employeeId}")
    public ResponseEntity<List<TaskDto>> getAllTasksWithEmployeeId(@PathVariable Long employeeId) {


        return ResponseEntity.ok(taskService.getAllTasksWithEmployeeId(employeeId));

    }

    @GetMapping("/withProject/{projectId}")
    public ResponseEntity<List<TaskDto>> getAllTasksWithProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasksWithProjectId(projectId));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
    }


}
