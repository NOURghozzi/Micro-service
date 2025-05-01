package com.userService.repository;

import com.userService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByDeletedFalse();
    Optional <User> findByEmailAndDeletedFalse(String email);
    Optional<User> findByIdAndDeletedFalse (Long id);

}
