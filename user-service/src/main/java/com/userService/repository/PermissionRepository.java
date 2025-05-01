package com.userService.repository;

import com.userService.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByDeletedFalse();
    Optional<Permission> findByIdAndDeletedFalse(Long id);
    List<Permission> findByRolesIdAndDeletedFalse(Long id);
    //List<Permission> findByDeletedFalseAndRole(Role roles);
}
