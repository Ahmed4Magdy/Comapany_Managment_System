package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<TaskDto> addTask(@RequestBody @Valid TaskDto dto) {

        TaskDto saved = taskService.addTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR', 'ROLE_EMPLOYEE')")
    public ResponseEntity<TaskDto> getTaskWithId(@PathVariable Long id) {


        return ResponseEntity.ok(taskService.getTaskWithId(id));


    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR')")
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTasks(@PathVariable Long id, @Valid @RequestBody TaskDto dto) {

        return ResponseEntity.ok(taskService.updateTasks(id, dto));


    }


    @GetMapping("/withEmployee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR', 'ROLE_EMPLOYEE')")

    public ResponseEntity<List<TaskDto>> getAllTasksWithEmployeeId(@PathVariable Long employeeId) {


        return ResponseEntity.ok(taskService.getAllTasksWithEmployeeId(employeeId));

    }
    @GetMapping("/withProject/{projectId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<TaskDto>> getAllTasksWithProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasksWithProjectId(projectId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
