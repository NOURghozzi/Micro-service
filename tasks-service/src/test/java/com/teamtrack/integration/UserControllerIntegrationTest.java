package com.teamtrack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamtrack.dto.RoleDTO;
import com.teamtrack.dto.UserDTO;
import com.teamtrack.entity.*;
import com.teamtrack.repository.CommentRepository;
import com.teamtrack.repository.RoleRepository;
import com.teamtrack.repository.TaskRepository;
import com.teamtrack.repository.UserRepository;
import com.teamtrack.service.RoleService;
import org.junit.After;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    User user;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Role role = Role.builder().name("Role")
                .build();

        role  = roleRepository.save(role);
        user= User.builder()
                .name("NO ACTIVE")
                .role(role)
                .email("nourghozzi9@gmail.com")
                .password("****")
                .preName("Ghozzi")
                .phone("28462286")
                .deleted(false)
                .build();
       user = userRepository.save(user);

    }

    @After
    public void setup(){
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public  void testAddUser() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        RoleDTO roleDTO1=  roleService.saveRole(roleDTO);
        UserDTO userDTO = UserDTO.builder()
                .name("nour")
                .role(roleDTO1)
                .email("nourghoz@gmail.com")
                .password("****")
                .preName("Ghozzi")
                .phone("28462286")
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    public void fetchUserById() throws Exception {
        mockMvc.perform(get("/user/find-user/" +user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(userRepository.findByIdAndDeletedFalse(user.getId()).isPresent());
    }

    @Test
    public void fetchAllUser() throws Exception {
        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }


    @Test
    public void testUpdateUser() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        RoleDTO roleDTO1=  roleService.saveRole(roleDTO);
        UserDTO userDTO = UserDTO.builder()
                .name("nour")
                .role(roleDTO1)
                .email("nourghoz@gmail.com")
                .password("****")
                .preName("Ghozzi")
                .phone("28462286")
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(put("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId())); }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/{id}", user.getId()))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetTasksByUser() throws Exception {
        Task task = Task.builder()
                .name("Add Project")
                .user(user)
                .build();
        Task task1 = Task.builder()
                .name("Delete Project")
                .user(user)
                .build();
       taskRepository.save(task);
       taskRepository.save(task1);

        mockMvc.perform(get("/user/tasks-user/{id}",user.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Delete Project")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Add Project")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

    @Test
    public void testGetCommentsByUser() throws Exception {
        Comment comment = Comment.builder()
                .message("project add")
                .user(user)
                .build();
        Comment comment1 = Comment.builder()
                .message("Project Deleted")
                .user(user)
                .build();
         commentRepository.save(comment1);
         commentRepository.save(comment);

        mockMvc.perform(get("/user/comments-user/{id}",user.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value("project add")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value("Project Deleted")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

}
