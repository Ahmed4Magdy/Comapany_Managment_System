package com.example.demo.dto;

import com.example.demo.entity.Task;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long employeeId;

    private Long projectId;

    @NotBlank(message = "Task title is required")
    private String title;
    @NotNull(message = "Deadline is required")
    private String description;
    @NotNull(message = "Deadline is required")
    private LocalDate deadline;


    private Task.TaskStatus status = Task.TaskStatus.PENDING;
}
