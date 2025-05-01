package com.teamtrack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teamtrack.dto.*;
import com.teamtrack.entity.Comment;
import com.teamtrack.entity.Sprint;
import com.teamtrack.entity.Task;
import com.teamtrack.repository.*;
import com.teamtrack.service.RoleService;
import com.teamtrack.service.SprintService;
import com.teamtrack.service.UserService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper;

    Task task ;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Sprint sprint = Sprint.builder().name("gestion de projet").build();
        sprint = sprintRepository.save(sprint);

        task = Task.builder()
                .name("app web")
                .sprint(sprint)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();
        task = taskRepository.save(task);
    }

    @Test
    public void testAddTask() throws Exception {
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.setName("gestion de projet");
        sprintDTO.setStartDate(LocalDateTime.parse("2018-12-30T19:34:50.63"));
        sprintDTO.setEndDate(LocalDateTime.parse("2019-12-30T19:34:50.63"));
        sprintDTO = sprintService.saveSprint(sprintDTO);

        TaskDTO taskDTO = TaskDTO.builder()
                .name("Authentification")
                .sprint(sprintDTO)
                .description("Authentification ")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void fetchTaskById() throws Exception {
        mockMvc.perform(get("/task/find-task/" +task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(sprintRepository.findByIdAndDeletedFalse(task.getId()).isPresent());
    }

    @Test
    public void fetchAllTask() throws Exception {
        mockMvc.perform(get("/task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testUpdateTask() throws Exception {
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.setName("gestion de projet");
        sprintDTO.setStartDate(LocalDateTime.parse("2018-12-30T19:34:50.63"));
        sprintDTO.setEndDate(LocalDateTime.parse("2019-12-30T19:34:50.63"));
        sprintDTO = sprintService.saveSprint(sprintDTO);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO=  roleService.saveRole(roleDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("nourghozi9@gmail.com");
        userDTO.setRole(roleDTO);
        userDTO = userService.saveUser(userDTO);
        TaskDTO taskDTO = TaskDTO.builder()
                .name("Authentification")
                .sprint(sprintDTO)
                .user(userDTO)
                .description("Authentification ")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();

        mockMvc.perform(put("/task/{id}",task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(task.getId())); }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/sprint/{id}", task.getId()))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetCommentsByTask() throws Exception {
        Comment comment = Comment.builder()
                .message("project add")
                .task(task)
                .build();
        Comment comment1 = Comment.builder()
                .message("Project Deleted")
                .task(task)
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment);

        mockMvc.perform(get("/task/comments-task/{id}",task.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value("Project Deleted")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value("project add")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

}
