package com.teamtrack.converter;

import com.teamtrack.dto.ProjectDTO;
import com.teamtrack.entity.Project;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    private static  ModelMapper modelMapper;

    @Autowired
    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    // Convert RoleDTO to Role entity
    public static Project dtoToModel(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

    // Convert Role entity to RoleDTO
    public static ProjectDTO modelToDto(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }
}
