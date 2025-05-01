package com.userService.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userService.dto.RoleDTO;
import com.userService.entity.Role;
import com.userService.repository.RoleRepository;
import com.userService.service.RoleService;
import org.junit.Before;
import org.junit.Test;
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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RoleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Role role= Role.builder()
                .name("NO ACTIVE")
                .deleted(false)
                .build();
        roleRepository.save(role);

    }

    @Test
    public void testAddRole() throws Exception {
        RoleDTO roleDTO= RoleDTO.builder()
                .name("NO ACTIVE")
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void fetchRoleById() throws Exception {
        mockMvc.perform(get("/role/find-role/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(roleRepository.findByIdAndDeletedFalse(1L).isPresent());
    }

    @Test
    public void fetchAllRole() throws Exception {
        mockMvc.perform(get("/role")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testUpdateRole() throws Exception {
        RoleDTO roleDTO= RoleDTO.builder()
                .name("Admin")
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(put("/role/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roleDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1)); // Ajoutez plus d'assertions selon la réponse attendue
    }

    @Test
    public void testDeleteRole() throws Exception {
        mockMvc.perform(delete("/role/{id}", 1L))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

}
