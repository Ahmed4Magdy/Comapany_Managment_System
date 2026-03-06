package com.example.demo.service;


import com.example.demo.dto.TaskDto;

import java.util.List;

public interface TaskService {


    TaskDto addTask(TaskDto dto);

    TaskDto getTaskWithId(Long id);

    List<TaskDto> getAllTasks();

    TaskDto updateTasks(Long id, TaskDto dto);

    void deleteTask(Long id);

    List<TaskDto> getAllTasksWithEmployeeId(Long employeeId);

    List<TaskDto> getAllTasksWithProjectId(Long projectId);


}
