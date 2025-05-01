package com.userService.repository;

import com.userService.dto.RoleDTO;
import com.userService.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository  <Role, Long> {

    List<Role> findByDeletedFalse();

    Optional <Role> findByIdAndDeletedFalse(Long id);

    Optional<Role> findByNameAndDeletedFalse(String name);

    RoleDTO findByName(String user);
}
