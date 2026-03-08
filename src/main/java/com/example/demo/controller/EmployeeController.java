package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping("/addEmployee")
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody EmployeeDto dto) {

        EmployeeDto saved = employeeService.addEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeWithId(@Valid @PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeWithId(id));

    }

    @GetMapping("/allEmployees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {

        return ResponseEntity.ok(employeeService.getAllEmployees());

    }



    @GetMapping("/ActiveTrue")
    public ResponseEntity<List<EmployeeDto>> getAllActiveTrueEmployees() {

        return ResponseEntity.ok(employeeService.getAllActiveTrueEmployees());
    }

    @GetMapping("/ActiveFalse")
    public ResponseEntity<List<EmployeeDto>> getAllActiveFalseEmployees() {
        return ResponseEntity.ok(employeeService.getAllActiveFalseEmployees());
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {

        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
