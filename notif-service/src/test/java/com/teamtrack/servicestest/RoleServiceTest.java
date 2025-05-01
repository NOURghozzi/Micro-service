package com.teamtrack.servicestest;

import com.teamtrack.TeamtrackApplication;
import com.teamtrack.dto.*;
import com.teamtrack.entity.Role;
import com.teamtrack.repository.RoleRepository;
import com.teamtrack.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService1;

    @Mock
    private RoleRepository roleRepository1;

    @Test
    public  void shouldSaveRoleSuccessfully(){
        RoleDTO roleDTO =  RoleDTO.builder()
                .name("nour")
                .build();
        Role role =  Role.builder()
                .name("nour")
                .build();
        // Mock repository behavior
        when(roleRepository1.save(any(Role.class))).thenReturn(role);
        RoleDTO savedRole = roleService1.saveRole(roleDTO);
        assertNotNull(savedRole, "Saved Role should not be null");
        assertEquals(roleDTO.getName(), savedRole.getName(), "Role name should match");

    }

    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnRole() {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(1L)
                .name("nour")
                .build();
        Role role = Role.builder()
                .id(1L)
                .name("nour")
                .build();
        when(roleRepository1.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(role));
        // Call the method under test
        RoleDTO found = roleService1.getRole(1L);
        assertNotNull(found, "Found Role should not be null");
        assertThat(found.getName()).isEqualTo(roleDTO.getName());
        assertThat(found.isDeleted()).isFalse();
 }
    @Test
    public void shouldWhenFindByIdAndDeletedFalse_thenReturnRoleList() {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(1L)
                .name("nour")
                .build();
        RoleDTO roleDTO1 = RoleDTO.builder()
                .id(2L)
                .name("nour1")
                .build();
        Role role = Role.builder()
                .id(1L)
                .name("nour")
                .build();
        Role role1 = Role.builder()
                .id(2L)
                .name("nour1")
                .build();
        when(roleRepository1.findByDeletedFalse()).thenReturn(Arrays.asList(role, role1));

        // Call the method under test
        List<RoleDTO> roleDTOS = roleService1.getAllRoles();


        // Verify interactions and assertions
        assertThat(roleDTOS).hasSize(2);
        assertThat(roleDTOS).extracting(RoleDTO::getName).containsExactlyInAnyOrder("nour", "nour1");

        // Verify that the repository method was called once
        verify(roleRepository1, times(1)).findByDeletedFalse();

    }
    @Test
    public void whenDeleteRoleThenRoleIsDeleted() {
        // given
        Long roleId = 4L;
        Role role = new Role();
        role.setId(roleId);
        role.setDeleted(false);

        // when
        when(roleRepository1.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        doAnswer(invocation -> {
            Role savedTask = invocation.getArgument(0);
            savedTask.setDeleted(true);
            return null;
        }).when(roleRepository1).save(any(Role.class));

        // Call the service method that should invoke deleteById
        roleService1.deleteRole(roleId);

        // then
        assertThat(role.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(roleRepository1).save(role);  // Verify that the save method was called
    }
    @Test
    public void testUpdateRoleSuccessfully() {
        Long roleId = 1L;
        Role role =  Role.builder()
                .id(roleId)
                .name("nour")
                .build();
        RoleDTO updatedUserDTO = new RoleDTO();
        updatedUserDTO.setName("New Name");

        when(roleRepository1.findByIdAndDeletedFalse(roleId)).thenReturn(Optional.of(role));
        when(roleRepository1.save(role)).thenReturn(role);

        Role updatedRole = roleService1.updateRole(role.getId(), updatedUserDTO);


        assertEquals(updatedUserDTO.getName(), updatedRole.getName());
       }
    @Test
    public void testUpdateRoleNotFound() {
        Long roleId = 1L;
        RoleDTO updatedRoleDTO = new RoleDTO();

        when(roleRepository1.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService1.updateRole(roleId, updatedRoleDTO);
        });

        assertEquals("Role with not found", exception.getMessage());
    }
    @Test
    public void testSaveRole_InvalidId_ThrowsException() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roleService1.saveRole(roleDTO);
        });
        assertEquals("Role ID must be null or 0 for new users.", exception.getMessage());

    }
}
