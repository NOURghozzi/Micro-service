package com.userService.controller;
import com.userService.dto.RoleDTO;
import com.userService.entity.Role;
import com.userService.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {
    private RoleService roleService;


    @GetMapping
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        log.info("Fetching all Role");
        return ResponseEntity.ok(this.roleService.getAllRoles());
    }

    @GetMapping("/find-role/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        log.info("Fetching Role");
        return ResponseEntity.ok(roleService.getRole(id));
               }

    @PostMapping
   // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDTO> saveRole(@Valid  @RequestBody RoleDTO roleDTO) {
        log.info("Saving Role: {}", roleDTO);
        return new ResponseEntity<>(roleService.saveRole(roleDTO),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
  //  @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id,@Valid @RequestBody RoleDTO updatedRole) {
        log.info("Updating Role: {} for ID: {}", updatedRole, id);

            return  ResponseEntity.ok(roleService.updateRole(id, updatedRole));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRole(@PathVariable Long id) {
        log.info("Deleting Team with ID: {}", id);
        roleService.deleteRole(id);

    }


}
