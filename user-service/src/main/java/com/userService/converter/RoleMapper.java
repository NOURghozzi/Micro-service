package com.userService.converter;

import com.userService.dto.RoleDTO;
import com.userService.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class RoleMapper {
    private static  ModelMapper modelMapper ;

    @Autowired
    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    // Convert RoleDTO to Role entity
    public static Role dtoToModel(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        return modelMapper.map(roleDTO, Role.class);
    }

    // Convert Role entity to RoleDTO
    public static RoleDTO modelToDto(Role role) {
        if (role == null) {
            return null;
        }
        return modelMapper.map(role, RoleDTO.class);
    }
}
