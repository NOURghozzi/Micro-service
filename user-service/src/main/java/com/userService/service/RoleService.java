package com.userService.service;

import com.userService.converter.RoleMapper;
import com.userService.dto.RoleDTO;
import com.userService.entity.Role;
import com.userService.exception.HttpCustomException;
import com.userService.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> getAllRoles() {
        log.info("Fetching all Roles");
        List<Role>role = roleRepository.findByDeletedFalse();
        return role.stream().map(RoleMapper::modelToDto).collect(Collectors.toList());
    }

    public RoleDTO getRole(Long id) {
        log.info("Fetching Role");
        return RoleMapper.modelToDto(roleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Role does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.info("Saving Role: {}", roleDTO);
        if ( (roleDTO.getId() != null)) {
               throw  new HttpCustomException("Role does not exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        roleRepository.findByNameAndDeletedFalse(roleDTO.getName())
                .ifPresent((test) -> {
                    throw new HttpCustomException("The Name " + test.getName() + " already exists.", HttpStatus.CONFLICT, HttpStatus.CONFLICT.value());
                });
    return RoleMapper.modelToDto(roleRepository.save(RoleMapper.dtoToModel(roleDTO)));
    }

    public void deleteRole(Long id) {
       Role role = roleRepository.findByIdAndDeletedFalse(id)
               .orElseThrow(() -> new HttpCustomException("Role does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
           role.setDeleted(true);
           roleRepository.save(role);
   }

    public Role updateRole(Long id, RoleDTO updatedRole) {
        log.info("Updating Role: {} for ID: {}", updatedRole, id);
        Role existingRole = roleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Role does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
            existingRole.setName(updatedRole.getName());
            return roleRepository.save(existingRole);

    }

}
