package com.example.demo.service.impl;

import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.exceptionhandling.ProjectHasTasksException;
import com.example.demo.exceptionhandling.ProjectNotFoundException;
import com.example.demo.mapper.ProjectMapper;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectDto addProject(ProjectDto dto) {

        Project project = projectMapper.toEntity(dto);
        Project saved = projectRepository.save(project);
        return projectMapper.toDto(saved);


    }

    @Override
    public ProjectDto getProjectWithId(Long id) {

        Project saved = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Not Found Project with " + id));
        return projectMapper.toDto(saved);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto dto) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Not Found Project with " + id));
        projectMapper.updateProjectFromDto(dto, project);
        Project updated = projectRepository.save(project);
        return projectMapper.toDto(updated);


    }

    @Override
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Not Found Project with " + id));
        //exist tasks with projectId
        boolean hasProjects = taskRepository.existsByProjectId(id);
        if (hasProjects) {
            throw new ProjectHasTasksException("Cannot delete project if tasks belong to it ");
        }

        projectRepository.deleteById(id);
    }
}
