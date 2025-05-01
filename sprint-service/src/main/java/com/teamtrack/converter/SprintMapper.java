package com.teamtrack.converter;

import com.teamtrack.dto.SprintDTO;
import com.teamtrack.entity.Sprint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SprintMapper {
    private static  ModelMapper modelMapper;
    @Autowired
    public SprintMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Convert RoleDTO to Role entity
    public static Sprint dtoToModel(SprintDTO sprintDTO) {
        return modelMapper.map(sprintDTO, Sprint.class);
    }

    // Convert Role entity to RoleDTO
    public static SprintDTO modelToDto(Sprint sprint) {
        return modelMapper.map(sprint, SprintDTO.class);
    }
}
