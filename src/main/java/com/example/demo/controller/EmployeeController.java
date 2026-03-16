package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.impl.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
//        try {
//            // 1. Authenticate email + password
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(),
//                            request.getPassword()
//                    )
//            );
//
//            // 2. If authenticated → generate JWT
//            if (authentication.isAuthenticated()) {
//                String token = jwtService.generateToken(request.getEmail());
//                return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
//            }
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Authentication failed");
//
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email or password");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred during login");
//        }


        return ResponseEntity.ok(employeeService.login(request));

    }

    @PostMapping("/addEmployee")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR')")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeDto dto) {

        EmployeeDto saved = employeeService.addEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR', 'ROLE_EMPLOYEE')")
    public ResponseEntity<EmployeeDto> getEmployeeWithId(@PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeWithId(id));

    }

    @GetMapping("/allEmployees")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {

        return ResponseEntity.ok(employeeService.getAllEmployees());

    }


    @GetMapping("/ActiveTrue")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_HR')")
    public ResponseEntity<List<EmployeeDto>> getAllActiveTrueEmployees() {

        return ResponseEntity.ok(employeeService.getAllActiveTrueEmployees());
    }

    @GetMapping("/ActiveFalse")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_HR')")

    public ResponseEntity<List<EmployeeDto>> getAllActiveFalseEmployees() {
        return ResponseEntity.ok(employeeService.getAllActiveFalseEmployees());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {

        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));


    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    // todo : add new endpoint to get all employees roles


}
