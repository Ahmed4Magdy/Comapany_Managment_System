package com.example.demo.mapper;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    Department toEntity(DepartmentDto dto);

    DepartmentDto toDto(Department entity);

    @Mapping(target = "id", ignore = true)
    void updateDepartmentFromDto(DepartmentDto dto, @MappingTarget Department entity);


}
