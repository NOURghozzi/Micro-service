package com.userService.service;

import com.userService.entity.Permission;
import com.userService.entity.Role;
import com.userService.entity.User;
import com.userService.exception.HttpCustomException;
import com.userService.repository.PermissionRepository;
import com.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkPassword(User user, String rawPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new HttpCustomException("User does not exist", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        Role role = user.getRole();

        authorities.add(new SimpleGrantedAuthority(role.getName()));

        List<Permission> permissions = permissionRepository.findByRolesIdAndDeletedFalse(role.getId());

        permissions.forEach(permission -> {
            authorities.add(new SimpleGrantedAuthority(permission.getEtat()));
        });

        return authorities;
    }
}
