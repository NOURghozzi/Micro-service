package com.userService.controller;
import com.userService.dto.*;
import com.userService.entity.User;
import com.userService.feign.MailClient;
import com.userService.service.MailService;
import com.userService.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private MailService mailService;

    @GetMapping("/all")
    //@PreAuthorize("hasAuthority('LIST_USER')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Fetching all Users");
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/find-user/{id}")
    //@PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("Fetching User");
        return ResponseEntity.ok(userService.getUser(id));
               }

   /* @GetMapping("/tasks-user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskDTO>> getTasksUser(@PathVariable Long id)  {
        log.info("Fetching Tasks for User with ID: {}", id);
        return ResponseEntity.ok(userService.getAllTasksUser(id));
    }

    @GetMapping("/comments-user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CommentDTO>> getCommentsUser(@PathVariable Long id)  {
        log.info("Fetching Comments for User with ID: {}", id);
        return ResponseEntity.ok(userService.getAllCommentsUser(id));
    }*/

    @PostMapping
   // @PreAuthorize("hasAuthority('ADD_USER')")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO User)  {
        log.info("Attempting to save User");
        return new ResponseEntity<>(userService.saveUser(User), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<User> updateUser(@PathVariable Long id,@Valid @RequestBody UserDTO updatedUser)  {
        log.info("Updating User: {} for ID: {}", updatedUser, id);
        return  ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    @PutMapping("/actif/{id}")
  //  @PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<User> actifUser(@PathVariable Long id)  {
        log.info("actif  User: {} for ID: {}", id);
        User updatedUser = userService.actifUser(id);
        mailService.sendActivationEmail(updatedUser,  "Your Account is Now Active",  "Dear " + updatedUser.getName() + ",\n\nYour account has been activated successfully.");
        return ResponseEntity.ok(updatedUser);
    }

   /* @PutMapping("/actif/{id}")
    //@PreAuthorize("hasAuthority('EDIT_USER')")
    public ResponseEntity<User> actifUser(@PathVariable Long id)  {
        log.info("actif  User: {} for ID: {}", id);
        User updatedUser = userService.actifUser(id);
        MailRequest emailRequest = new MailRequest();

        emailRequest.setTo(updatedUser);
        emailRequest.setSubject("Your Account is Now Active");
        emailRequest.setBody("Dear " + updatedUser.getName() + ",\n\nYour account has been activated successfully.");

      //  mailClient.sendMail(emailRequest);
        return ResponseEntity.ok(updatedUser);
    }*/


    @DeleteMapping("/{id}")
   // @PreAuthorize("hasAuthority('DELETE_USER')")
    public void deleteUser(@PathVariable Long id) {
        log.info("Delete User with ID: {}", id);
        userService.deleteUser(id);

     }




}


