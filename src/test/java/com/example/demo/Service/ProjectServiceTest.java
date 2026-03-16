package com.example.demo.Service;

import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.Project;
import com.example.demo.exceptionhandling.ProjectNotFoundException;
import com.example.demo.mapper.ProjectMapper;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectMapper projectMapper;
    @InjectMocks
    private ProjectServiceImpl projectServiceimpl;

    private Project project;
    private ProjectDto dto;

    @BeforeEach
    void setup() {
        dto = new ProjectDto();
        dto.setName("Web application");
        dto.setStartDate(LocalDate.of(2026, 02, 28));
        dto.setEndDate(LocalDate.of(2026, 03, 28));

        project = new Project();
        project.setName("Web application");
        project.setStartDate(LocalDate.of(2026, 02, 28));
        project.setEndDate(LocalDate.of(2026, 03, 28));
    }

    @Test
    void addProject_ShouldReturnProjectSuccessfully() {


        when(projectMapper.toEntity(dto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(dto);


        ProjectDto result = projectServiceimpl.addProject(dto);
        assertNotNull(result);
        assertEquals("Web application",result.getName());
        verify(projectMapper,times(1)).toDto(project);


    }

    @Test
    void getProjectWithId_shouldReturnProjectWithSuccessfully() {



        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(dto);


        ProjectDto result = projectServiceimpl.getProjectWithId(anyLong());
        assertNotNull(result);
        assertEquals("Web application", result.getName());


    }


    @Test
    void getProjectWithId_shouldReturnProjectNotFoundException() {

        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ProjectNotFoundException.class, () -> {
            projectServiceimpl.getProjectWithId(1L);
        });


        verify(projectMapper, never()).toDto(any());


    }

    @Test
    void getAllProjects_ShouldReturnAllProjectsWithSuccessfully() {


        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(dto);

        List<ProjectDto> projects = projectServiceimpl.getAllProjects();

        assertNotNull(projects);
        assertEquals(1, projects.size());


    }

    @Test
    void updateProject_shouldReturnUpdatedProject() {

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        doNothing().when(projectMapper).updateProjectFromDto(dto, project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(dto);

        ProjectDto result = projectServiceimpl.updateProject(1L, dto);
        assertNotNull(result);
        assertEquals(dto, result);


    }

    @Test
    void updateProject_shouldReturnProjectNotFoundException() {

        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ProjectNotFoundException.class, () -> {
            projectServiceimpl.updateProject(1L, dto);
        });

        assertEquals("Not Found Project with " + 1, ex.getMessage());
        verify(projectMapper, never()).updateProjectFromDto(any(), any());


    }

    @Test
    void deleteProject_shouldDeletedProjectWithSuccessfully() {

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).deleteById(1L);
        projectServiceimpl.deleteProject(1L);
        verify(projectRepository, times(1)).deleteById(1L);


    }



    @Test
    void deleteProject_shouldThrowProjectNotFoundException(){


        when(projectRepository.findById(1L)).thenReturn(Optional.empty());


        Exception ex = assertThrows(ProjectNotFoundException.class, () -> {
            projectServiceimpl.deleteProject(1L);
        });

        assertEquals("Not Found Project with " + 1, ex.getMessage());

    }




}
