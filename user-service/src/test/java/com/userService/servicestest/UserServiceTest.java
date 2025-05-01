package com.userService.servicestest;
import com.userService.TeamtrackApplication;
import com.userService.dto.RoleDTO;
import com.userService.dto.UserDTO;
import com.userService.entity.Role;
import com.userService.entity.User;
import com.userService.repository.UserRepository;
import com.userService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService1;

    @Mock
    private UserRepository userRepository1;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public  void shouldSaveUserSuccessfully(){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        UserDTO userDTO = UserDTO.builder()
                .name("nour")
                .email("nour@gmail.com")
                .password("1234567")
                .phone("28462286")
                .preName("ghozzi")
                .role(roleDTO)
                .build();
        Role role = new Role();
        role.setId(1L);
        User user = User.builder()
                .name("nour")
                .email("nour@gmail.com")
                .password("1234567")
                .phone("28462286")
                .preName("ghozzi")
                .role(role)
                .build();
        when(userRepository1.save(any(User.class))).thenReturn(user);

        UserDTO saveuser = userService1.saveUser(userDTO);
        assertNotNull(saveuser);
        assertEquals(saveuser.getName(),userDTO.getName());
    }

    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnUser() {

        Role role = new Role();
        User user = User.builder()
                .id(1L)
                .name("nour")
                .email("ghozzi@gmail.com")
                .password("1234567")
                .phone("28462286")
                .preName("ghozzi")
                .role(role)
                .deleted(false)
                .build();
        // Mock the repository behavior
        when(userRepository1.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(user));

        // Call the method under test
        UserDTO found = userService1.getUser(1L);

        assertNotNull(found);
        assert user != null;
        assertThat(found.getName()).isEqualTo(user.getName());
        assertThat(found.isDeleted()).isFalse();
    }

    @Test
    public void shouldWhenFindByIdAndDeletedFalse_thenReturnUserList() {
        RoleDTO roleDTO = new RoleDTO();
        Role role = new Role();
        roleDTO.setId(1L);

        User user = User.builder()
                .id(1L)
                .name("nour")
                .email("nour50@gmail.com")
                .password("1234567")
                .phone("28462286")
                .preName("ghozzi")
                .role(role)
                .build();

        User user1 = User.builder()
                .id(2L)
                .name("nour2")
                .email("nour50@gmail.com")
                .password("1234567")
                .phone("28462286")
                .preName("ghozzi")
                .role(role)
                .build();
        when(userRepository1.findByDeletedFalse()).thenReturn(Arrays.asList(user1, user));
        List<UserDTO> usersDTO = userService1.getAllUsers();
        assertThat(usersDTO).hasSize(2);
        assertThat(usersDTO).extracting(UserDTO::getName).containsExactlyInAnyOrder("nour", "nour2");
        // Verify that the repository method was called once
        verify(userRepository1, times(1)).findByDeletedFalse();

    }

    @Test
    public void whenDeleteUserThenUserIsDeleted() {
        Long userId = 1L;
        // given
        User user = new User();
        user.setId(userId);
        user.setDeleted(false);

        // when
        when(userRepository1.findByIdAndDeletedFalse(userId)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            User saveduser = invocation.getArgument(0);
           saveduser.setDeleted(true);
           return null;
        }).when(userRepository1).save(any(User.class));

        // Call the service method that should invoke deleteById
        userService1.deleteUser(userId);

        // then
        assertThat(user.isDeleted()).isTrue();  // Verify that the team is marked as deleted
    }
    @Test
    public void testUpdateUserSuccess() {
        Long userId = 1L;
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        Role role = new Role();
        role.setId(1L);
        User user = User.builder()
                .id(1L)
                .name("nour")
                .email("nourghozzi0209@gmail.com")
                .password("1234567")
                .phone("28462286")
                .preName("ghozzi")
                .role(role)
                .build();
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName("New Name");
        updatedUserDTO.setEmail("new@example.com");
        updatedUserDTO.setPassword("newpassword");
        updatedUserDTO.setPhone("0987654321");
        updatedUserDTO.setRole(roleDTO);
        updatedUserDTO.setPreName("roleDTO");

        when(userRepository1.findByIdAndDeletedFalse(userId)).thenReturn(Optional.of(user));
        when(userRepository1.save(user)).thenReturn(user);

        User updatedUser = userService1.updateUser(user.getId(), updatedUserDTO);


        assertEquals(updatedUserDTO.getName(), updatedUser.getName());
        assertEquals(updatedUserDTO.getEmail(), updatedUser.getEmail());
        assertEquals(updatedUserDTO.getPassword(), updatedUser.getPassword());
        assertEquals(updatedUserDTO.getPhone(), updatedUser.getPhone());
    }
    @Test
    public void testUpdateUserNotFound() {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO();

        when(userRepository1.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService1.updateUser(userId, updatedUserDTO);
        });

        assertEquals("User not found", exception.getMessage());
    }
    @Test
    public void testSaveTasks_InvalidId_ThrowsException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L); // Invalid ID

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService1.saveUser(userDTO);
        });
        assertEquals("User ID must be null or 0 for new users.", exception.getMessage());

    }
    @Test
    public void testSaveUserWithNullEmail() {
        UserDTO userDTO = new UserDTO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService1.saveUser(userDTO);
        });

        assertEquals("User email must not be null.", exception.getMessage());
    }
    @Test
    public void testSaveUserWithExistingId() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("new@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService1.saveUser(userDTO);
        });

        assertEquals("User ID must be null or 0 for new users.", exception.getMessage());
    }



}
