package com.userService.controller;

import com.userService.dto.PermissionDTO;
import com.userService.entity.Permission;
import com.userService.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/permission")
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;
    // READ_Permission : Admin And le Manager
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        log.info("Fetching all Permission");
        return ResponseEntity.ok(this.permissionService.getAllPermissions());
    }

    @GetMapping("/find-permission/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable Long id) {
        log.info("Fetching Permission");
        return ResponseEntity.ok(permissionService.getPermission(id));
    }

    @GetMapping("/role-permission/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PermissionDTO>> getPermissionByRole(@PathVariable Long id) {
        log.info("Fetching permission for Role with ID: {}", id);
        return ResponseEntity.ok(permissionService.getAllPermissionsByRole(id));}

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PermissionDTO> savePermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        log.info("Attempting to save Permission");
        return new ResponseEntity<>(permissionService.savePermission(permissionDTO),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Permission> updatePermission( @PathVariable Long id,@Valid @RequestBody PermissionDTO updatedPermission) {
        log.info("Updating Permission: {} for ID: {}", updatedPermission, id);
            return  ResponseEntity.ok(permissionService.updatePermission(id, updatedPermission));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePermission(@PathVariable Long id) {
        log.info("Deleting Permission with ID: {}", id);
        permissionService.deletePermission(id);

    }


}

