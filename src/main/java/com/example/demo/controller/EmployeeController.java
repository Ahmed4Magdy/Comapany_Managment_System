package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.impl.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(authRequest.getEmail());

            return ResponseEntity.ok(new AuthResponse(token, "Login successful"));

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            // هنا هندل الاستثناء مباشرة ونرجع 401
            AuthResponse error = new AuthResponse(null, "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
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

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Role> getAllRoles() {
        return Arrays.asList(Role.values());
    }
}
