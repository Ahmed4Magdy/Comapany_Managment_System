package com.example.demo.controller;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody @Valid DepartmentDto dto) {

        DepartmentDto saved = departmentService.createDepartment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }


    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentWithId(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentWithId(id));
    }

    @GetMapping("")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto dto) {

        return ResponseEntity.ok(departmentService.updateDepartment(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
