package com.userService.servicestest;

import com.userService.TeamtrackApplication;
import com.userService.dto.*;
import com.userService.entity.*;
import com.userService.repository.PermissionRepository;
import com.userService.service.PermissionService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldSavePermissionsSuccessfully() {
         RoleDTO roleDTO =  new  RoleDTO();
         List<RoleDTO> rolesDtos = List.of(roleDTO);
         PermissionDTO permissionDTO = PermissionDTO.builder()
                 .etat("test")  // Ensure the field name matches the one in your DTO
                 .roles(rolesDtos)
                 .build();

        // Prepare entity corresponding to DTO
        Role role = new Role();
        List<Role> roles1 =List.of(role);
         Permission permission =  Permission.builder()
                 .etat("test")  // Ensure the field name matches the one in your DTO
                 .roles(roles1)
                 .deleted(false)
                 .build();


        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        PermissionDTO savedPermissionDTO = permissionService.savePermission(permissionDTO);

        assertNotNull(savedPermissionDTO);
        assertEquals(permissionDTO.getEtat(), savedPermissionDTO.getEtat(), "Permission name should match");

    }

    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnPermissions() {
         RoleDTO roleDTO = new  RoleDTO();
        List<RoleDTO> roles = List.of(roleDTO);
         PermissionDTO permissionDTO =  PermissionDTO.builder()
                .id(1L)
                .roles(roles)
                .etat("test")  // Ensure the field name matches the one in your DTO
                .build();

        // Prepare entity corresponding to DTO
      Role role = new Role();
      List<Role> roles1 = List.of(role);
         Permission permission =  Permission.builder()
                 .id(1L)
                 .roles(roles1)
                 .etat("test")  // Ensure the field name matches the one in your DTO
                 .build();
        when(permissionRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(permission));

        // Call the method under test
        PermissionDTO found = permissionService.getPermission(1L) ;

        // Verify interactions and assertions
        assertNotNull(found, "Found permission should not be null");
        assertThat(found.getEtat()).isEqualTo(permissionDTO.getEtat());
        assertThat(found.isDeleted()).isFalse();

        // Verify that the repository method was called with the correct argument
        verify(permissionRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    public void shouldWhenGetAllTasks_thenReturnPermissionsList() {

        // Prepare entity corresponding to DTO
        Role role = new Role();
        List<Role> roles1 = List.of(role);
        Permission permission =  Permission.builder()
                .id(1L)
                .roles(roles1)
                .etat("test")  // Ensure the field name matches the one in your DTO
                .build();
        Permission permission1 =  Permission.builder()
                .id(1L)
                .roles(roles1)
                .etat("test1")  // Ensure the field name matches the one in your DTO
                .build();

        // Mock the repository method to return a list of permissions
        when(permissionRepository.findByDeletedFalse()).thenReturn(Arrays.asList(permission1, permission));

        // Call the method under test
        List<PermissionDTO> permissions = permissionService.getAllPermissions();

        // Verify interactions and assertions
        assertThat(permissions).hasSize(2);
        assertThat(permissions).extracting( PermissionDTO::getEtat).containsExactlyInAnyOrder("test", "test1");

        // Verify that the repository method was called once
        verify(permissionRepository, times(1)).findByDeletedFalse();
    }
    @Test
    public void whenDeletePermissionsThenPermissionsIsDeleted() {
        Long permissionId = 4L;
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setDeleted(false);

        // when
        when(permissionRepository.findByIdAndDeletedFalse(permissionId)).thenReturn(Optional.of(permission));
        doAnswer(invocation -> {
            Permission savedPermission = invocation.getArgument(0);
            savedPermission.setDeleted(true);
            return null;
        }).when(permissionRepository).save(any(Permission.class));

        // Call the service method that should invoke deleteById
        permissionService.deletePermission(permissionId);

        // then
        assertThat(permission.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(permissionRepository).save(permission);  // Verify that the save method was called
    }
    @Test
    public void testUpdatePermissionsSuccess() {
        Long permissionId = 1L;
        RoleDTO roleDTO = new RoleDTO();
        List<RoleDTO> roleDTOList = List.of(roleDTO);
        Role role = new  Role();
        List<Role> roles = List.of(role);
        Permission permission =  Permission.builder()
                .id(permissionId)
                .roles(roles)
                .etat("test")  // Ensure the field name matches the one in your DTO
                .build();
        PermissionDTO updatedPermissionDTO = new PermissionDTO();
        updatedPermissionDTO.setEtat("New etat");
        updatedPermissionDTO.setRoles(roleDTOList);

        when(permissionRepository.findByIdAndDeletedFalse(permissionId)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(permission)).thenReturn(permission);

        Permission updatedPermission = permissionService.updatePermission((permission.getId()),updatedPermissionDTO);
        assertEquals(updatedPermissionDTO.getEtat(), updatedPermission.getEtat());
    }

    @Test
    public void testUpdatePermissionNotFound() {
        Long permissionId = 1L;
        PermissionDTO updatedPermissionDTO = new PermissionDTO();

        when(permissionRepository.findByIdAndDeletedFalse(permissionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            permissionService.updatePermission( permissionId, updatedPermissionDTO);
        });

        assertEquals("Permission does not exists", exception.getMessage());
    }
    @Test
    public void testSavePermission_InvalidId_ThrowsException() {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.savePermission(permissionDTO);
        });
        assertEquals("permission ID must be null or 0 for new users.", exception.getMessage());

    }
}
