package com.example.demo.controller;

import com.example.demo.dto.ProjectDto;
import com.example.demo.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;


    @PostMapping("/create")
    public ResponseEntity<ProjectDto> addProject(@Valid @RequestBody ProjectDto dto) {

        ProjectDto saved = projectService.addProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);


    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectWithId(@PathVariable Long id) {

        return ResponseEntity.ok(projectService.getProjectWithId(id));
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDto dto) {

        return ResponseEntity.ok(projectService.updateProject(id, dto));


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
