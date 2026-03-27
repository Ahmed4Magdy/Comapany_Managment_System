package com.example.demo.mapper;


import com.example.demo.dto.ProjectDto;
import com.example.demo.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

//    @Mapping(target = "id", ignore = true)
    Project toEntity(ProjectDto dto);


    ProjectDto toDto(Project entity);

    @Mapping(target = "id", ignore = true)
    void updateProjectFromDto(ProjectDto dto, @MappingTarget Project entity);

}
