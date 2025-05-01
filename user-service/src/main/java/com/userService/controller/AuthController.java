package com.userService.controller;

import com.userService.dto.LoginDTO;
import com.userService.dto.TokenDTO;
import com.userService.entity.User;
import com.userService.exception.HttpCustomException;
import com.userService.repository.UserRepository;
import com.userService.service.CustomUserService;
import com.userService.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/authentication")
@AllArgsConstructor
public class AuthController {

    private  AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtUtil;

    private  CustomUserService customUserService;

    private UserRepository userRepository;



    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO authRequest) {
         log.info("test"+authRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
             log.info("test1"+authRequest.getEmail());
             User user = userRepository.findByEmailAndDeletedFalse(authRequest.getEmail())
                     .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
             UserDetails userDetails = customUserService.loadUserByUsername(authRequest.getEmail());
            log.info("test2"+userDetails.getUsername());
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities(),user.getRole().getName(),user.getId());

            return new TokenDTO(jwtToken);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials", e);
        }
    }
}