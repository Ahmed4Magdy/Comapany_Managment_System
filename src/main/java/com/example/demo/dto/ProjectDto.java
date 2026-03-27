package com.example.demo.dto;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectDto {

    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "end date is required")
    private LocalDate endDate;

    private Project.ProjectStatus status = Project.ProjectStatus.NOT_STARTED;





}
