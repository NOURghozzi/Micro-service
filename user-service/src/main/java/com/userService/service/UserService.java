package com.userService.service;

import com.userService.converter.*;
import com.userService.dto.UserDTO;
import com.userService.entity.User;
import com.userService.exception.HttpCustomException;
import com.userService.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public final BCryptPasswordEncoder bCryptPasswordEncoder;
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all User");
        List<User> users = userRepository.findByDeletedFalse();
        return users.stream().map(UserMapper::modelToDto).collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        log.info("Fetching User");
        return UserMapper.modelToDto(userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

    /*public List<TaskDTO> getAllTasksUser(Long id) {
        log.info("Fetching Tasks for User with ID: {}", id);
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        List<Task> tasksUser = taskRepository.findByUserIdAndDeletedFalse(user.getId());
        return tasksUser.stream()
                .map(TaskMapper::modelToDto)
                .sorted(Comparator.comparing(TaskDTO::getId).reversed())
                .collect(Collectors.toList());

    }

    public List<CommentDTO> getAllCommentsUser(Long id) {
        log.info("Fetching Comments for User with ID: {}", id);
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        List<Comment> commentsUser = commentRepository.findByUserIdAndDeletedFalse(user.getId());

        return commentsUser.stream()
                .map(CommentMapper::modelToDto)
                .sorted(Comparator.comparing(CommentDTO::getId).reversed())
                .collect(Collectors.toList());

    }*/

    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Saving User: {}", userDTO);

        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        userDTO.setState("inactif");

        if ((userDTO.getId() != null)) {
            throw new HttpCustomException("User does not exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        userRepository.findByEmailAndDeletedFalse(userDTO.getEmail()).ifPresent(user -> {

            throw new HttpCustomException("The email " + userDTO.getEmail() + " already exists.", HttpStatus.CONFLICT, HttpStatus.CONFLICT.value());

        });

        return UserMapper.modelToDto(userRepository.save(UserMapper.dtoToModel(userDTO)));
    }

    public User updateUser(Long id, UserDTO updatedUserDTO) {
        log.info("Updating user: {} for ID: {}", updatedUserDTO, id);

        User existingUser = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("User does not exist", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        existingUser.setName(updatedUserDTO.getName());
        existingUser.setEmail(updatedUserDTO.getEmail());
        existingUser.setPassword(updatedUserDTO.getPassword());
        existingUser.setPhone(updatedUserDTO.getPhone());
        existingUser.setSpecialityIds(updatedUserDTO.getSpeciality());
        existingUser.setRole(RoleMapper.dtoToModel(updatedUserDTO.getRole()));
        return userRepository.save(existingUser);
    }
    public User actifUser(Long id) {
        log.info("actif User : {} for ID: {}", id);
        User existingUser = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        if (!existingUser.getState().equals("actif")){
        existingUser.setState("actif");}
        else{
            existingUser.setState("inactif");
        }
        return userRepository.save(existingUser);
    }




    public void deleteUser(Long id) {
        log.info("Deleting User with ID: {}", id);
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        user.setDeleted(true);
        userRepository.save(user);

    }

    public String login(String email, String pass) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new HttpCustomException("User does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        if ((user != null)) {
            String password = user.getPassword();
            if (password.equals(pass)) {
                log.info("Success");
                return "Success";
            } else {
                return "password not match";
            }
        } else {
            log.info("Password");
            return "email not exist";

        }

    }

    /* **************************************************************** */


}
