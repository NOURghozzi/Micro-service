package com.teamtrack.converter;

import com.teamtrack.dto.TeamDTO;
import com.teamtrack.entity.Team;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    private static  ModelMapper modelMapper ;
    @Autowired
    public TeamMapper(ModelMapper modelMapper) {
        TeamMapper.modelMapper = modelMapper;
    }

    // Convert RoleDTO to Role entity
    public static Team dtoToModel(TeamDTO teamDTO) {
        return modelMapper.map(teamDTO, Team.class);
    }

    // Convert Role entity to RoleDTO
    public static TeamDTO modelToDto(Team team) {
        return modelMapper.map(team, TeamDTO.class);
    }
}
