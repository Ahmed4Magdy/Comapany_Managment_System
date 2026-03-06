package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.ProjectDto;

import java.util.List;

public interface ProjectService {


    ProjectDto addProject(ProjectDto dto);

    ProjectDto getProjectWithId(Long id);

    List<ProjectDto> getAllProjects();

    ProjectDto updateProject(Long id,ProjectDto dto);

    void deleteProject(Long id);

}
