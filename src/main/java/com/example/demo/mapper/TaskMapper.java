package com.example.demo.mapper;

import com.example.demo.dto.TaskDto;
import com.example.demo.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskDto dto);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "projectId", source = "project.id")
    TaskDto toDto(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "project", ignore = true)
    void updateTaskFromdto(TaskDto dto, @MappingTarget Task entity);
}
