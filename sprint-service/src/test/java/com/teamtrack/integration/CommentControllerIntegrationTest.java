package com.teamtrack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teamtrack.dto.*;
import com.teamtrack.entity.*;
import com.teamtrack.repository.*;
import com.teamtrack.service.CommentService;
import com.teamtrack.service.RoleService;
import com.teamtrack.service.TaskService;
import com.teamtrack.service.UserService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CommentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    private ObjectMapper objectMapper;

    Comment comment;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Task task = Task.builder()
                .name("ajouter sprint")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .build();
        task = taskRepository.save(task);
        Role role = new Role();
        role = roleRepository.save(role);
        User user = User.builder().name("Nour")
                .build();
        user = userRepository.save(user);

        comment = Comment.builder()
                 .message("cc bb")
                 .task(task)
                 .user(user)
                 .deleted(false)
                 .build();
        comment = commentRepository.save(comment);
    }

    @After
    public void setup(){
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public  void testAddComment() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO = roleService.saveRole(roleDTO);
        TaskDTO taskDTO = TaskDTO.builder()
                .name("ajouter sprint")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .build();
        taskDTO=taskService.saveTasks(taskDTO);
        UserDTO userDTO = UserDTO.builder()
                .name("Nour")
                .email("nourghozzi9@gmail.com")
                .role(roleDTO)
                .build();
        userDTO = userService.saveUser(userDTO);
        CommentDTO commentDTO = CommentDTO.builder()
                .message("cc")
                .user(userDTO)
                .task(taskDTO)
                .deleted(false)
                .build();
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    public void fetchCommentById() throws Exception {
        mockMvc.perform(get("/comment/find-comment/" +comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(commentRepository.findByIdAndDeletedFalse(comment.getId()).isPresent());
    }

    @Test
    public void fetchAllComment() throws Exception {
        mockMvc.perform(get("/comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/comment/{id}", comment.getId()))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testUpdateComment() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO = roleService.saveRole(roleDTO);
        TaskDTO taskDTO = TaskDTO.builder()
                .name("ajouter sprint")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .build();
        taskDTO=taskService.saveTasks(taskDTO);
        UserDTO userDTO = UserDTO.builder()
                .name("Nour")
                .email("nourghozzi9@gmail.com")
                .role(roleDTO)
                .build();
        userDTO = userService.saveUser(userDTO);
        CommentDTO commentDTO = CommentDTO.builder()
                .message("cc")
                .user(userDTO)
                .task(taskDTO)
                .deleted(false)
                .build();

        mockMvc.perform(put("/comment/{id}",comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(comment.getId())); }

}
