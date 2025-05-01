package com.userService.converter;

import com.userService.dto.PermissionDTO;
import com.userService.entity.Permission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    private static  ModelMapper modelMapper;
    @Autowired
    public PermissionMapper(ModelMapper modelMapper) {
        PermissionMapper.modelMapper = modelMapper;
    }
    public static Permission dtoToModel(PermissionDTO permissionDTO) {
        return modelMapper.map(permissionDTO, Permission.class);
    }

    // Convert Role entity to RoleDTO
    public static PermissionDTO modelToDto(Permission permission) {
        return modelMapper.map(permission, PermissionDTO.class);
    }
}
