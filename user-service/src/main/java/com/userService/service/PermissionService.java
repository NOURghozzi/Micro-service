package com.userService.service;

import com.userService.converter.PermissionMapper;
import com.userService.converter.RoleMapper;
import com.userService.dto.PermissionDTO;
import com.userService.entity.Permission;
import com.userService.entity.Role;
import com.userService.exception.HttpCustomException;
import com.userService.repository.PermissionRepository;
import com.userService.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private RoleRepository roleRepository;

    public List<PermissionDTO> getAllPermissions() {
        log.info("Fetching all Permission");
        List<Permission> permissions = permissionRepository.findByDeletedFalse();
        return permissions.stream().map(PermissionMapper::modelToDto).collect(Collectors.toList());
    }

    public List<PermissionDTO> getAllPermissionsByRole(Long id) {

       log.info("Fetching Permissions for Role with ID: {}", id);

       Role role = roleRepository.findByIdAndDeletedFalse(id)
               .orElseThrow(() -> new HttpCustomException("Role does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
       List<Permission> permissions = permissionRepository.findByRolesIdAndDeletedFalse(role.getId());

       return permissions.stream()
                .map(PermissionMapper::modelToDto)
                .collect(Collectors.toList());
     }

    public PermissionDTO getPermission(Long id) {
        return PermissionMapper.modelToDto(permissionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Permission does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

    public PermissionDTO savePermission(PermissionDTO permissionDTO) {
        if ( (permissionDTO.getId() != null)) {
                throw  new HttpCustomException("Permission does not exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        return PermissionMapper.modelToDto(permissionRepository.save(PermissionMapper.dtoToModel(permissionDTO)));
    }

    public Permission updatePermission(Long id, PermissionDTO updatedPermissionDTO) {
        log.info("Updating Permission: {} for ID: {}", updatedPermissionDTO, id);
        Permission existingPermission = permissionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Permission does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
            existingPermission.setEtat(updatedPermissionDTO.getEtat());
            existingPermission.setRoles(updatedPermissionDTO.getRoles().stream()
                        .map(RoleMapper::dtoToModel)
                        .collect(Collectors.toList())
            );
        return permissionRepository.save(existingPermission);

    }

    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Permission does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        permission.setDeleted(true);
        permissionRepository.save(permission);

    }


}