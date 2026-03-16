package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface EmployeeMapper {


    @Mapping(target = "department", ignore = true)
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeDto dto);


    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "departmentName",source = "department.name")
    EmployeeDto toDto(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)   // ✅ ignore department
    void updateEmployeeFromDto(EmployeeDto dto, @MappingTarget Employee entity);


}
