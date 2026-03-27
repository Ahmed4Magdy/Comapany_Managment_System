package com.example.demo.dto;



import com.example.demo.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private Long id;

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be greater than 0")
    private Long departmentId;

    private String departmentName;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Position is required")
    private String position;

    private boolean active = true;

    private LocalDate hireDate;

    @JsonProperty("employee_role")
    @NotNull
    private Role role;


}
