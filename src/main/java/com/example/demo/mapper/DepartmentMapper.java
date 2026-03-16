package com.example.demo.mapper;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(DepartmentDto dto);

    DepartmentDto toDto(Department entity);

    void updateDepartmentFromDto(DepartmentDto dto, @MappingTarget Department entity);


}
