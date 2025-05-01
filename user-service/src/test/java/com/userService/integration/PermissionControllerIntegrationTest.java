package com.userService.integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userService.dto.PermissionDTO;
import com.userService.entity.Permission;
import com.userService.entity.Role;
import com.userService.repository.PermissionRepository;
import com.userService.repository.RoleRepository;
import com.userService.service.PermissionService;
import com.userService.service.RoleService;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PermissionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    Permission  permission;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Role role = Role.builder().name("Role")
                .build();

        role  = roleRepository.save(role);
          permission = Permission.builder()
                .etat("INACTIVE")
                .roles(List.of(role)) // Utiliser des données mockées ou des valeurs fictives
                .deleted(false)
                .build();
        permissionRepository.save(permission);
    }

    @Test
    public  void testAddPermission() throws Exception {
        PermissionDTO permissionDTO = PermissionDTO.builder()
                .etat("NO ACTIVE")
                .roles(new ArrayList<>())
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/permission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(permissionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }
    @Test
    public void fetchPermissionById() throws Exception {
        mockMvc.perform(get("/permission/find-permission/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

       assert(permissionRepository.findByIdAndDeletedFalse(1L).isPresent());
    }
    @Test
    public void fetchAllPermission() throws Exception {
        mockMvc.perform(get("/permission")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }
    @Test
    public void testUpdatePermission() throws Exception {
        PermissionDTO permissionDTO = PermissionDTO.builder()
                .etat("NO ACTIVE")
                .roles(new ArrayList<>())
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/permission/{id}", permission.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(permissionDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(permission.getId())); // Ajoutez plus d'assertions selon la réponse attendue
    }
    @Test
    public void testDeletePermission() throws Exception {
        mockMvc.perform(delete("/permission/{id}", 1L))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void testGetPermissionByRole() throws Exception {

        Role role = Role.builder().name("Role")
        .build();

        role  = roleRepository.save(role);
        Permission  permission = Permission.builder()
                .etat("INACTIVE")
                .roles(List.of(role)) // Utiliser des données mockées ou des valeurs fictives
                .deleted(false)
                .build();
        Permission  permission2 = Permission.builder()
                .etat("ACTIVE")
                .roles(List.of(role)) // Utiliser des données mockées ou des valeurs fictives
                .deleted(false)
                .build();

        permissionRepository.save(permission2);
        permissionRepository.save(permission);
        mockMvc.perform(get("/permission/role-permission/{id}", role.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].etat").value("ACTIVE")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].etat").value("INACTIVE")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

}
