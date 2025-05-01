package com.teamtrack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teamtrack.dto.ProjectDTO;
import com.teamtrack.dto.SprintDTO;
import com.teamtrack.entity.Project;
import com.teamtrack.entity.Sprint;
import com.teamtrack.entity.Task;
import com.teamtrack.repository.ProjectRepository;
import com.teamtrack.repository.SprintRepository;
import com.teamtrack.repository.TaskRepository;
import com.teamtrack.service.ProjectService;
import com.teamtrack.service.SprintService;
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
public class SprintControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskRepository taskRepository;

    private ObjectMapper objectMapper;
    Sprint sprint ;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Project project = Project.builder().name("Role").build();
        project = projectRepository.save(project);

         sprint = Sprint.builder()
                .name("app web")
                .project(project)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();
        sprint = sprintRepository.save(sprint);
    }

    @Test
    public void testAddSprint() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Role");
        projectDTO.setStartDate(LocalDateTime.parse("2018-12-30T19:34:50.63"));
        projectDTO.setEndDate(LocalDateTime.parse("2019-12-30T19:34:50.63"));
        projectDTO = projectService.saveProject(projectDTO);

        SprintDTO sprintDTO = SprintDTO.builder()
                .name("app web")
                .project(projectDTO)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();

        mockMvc.perform(post("/sprint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sprintDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void fetchSprintById() throws Exception {
        mockMvc.perform(get("/sprint/find-sprint/" +sprint.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(sprintRepository.findByIdAndDeletedFalse(sprint.getId()).isPresent());
    }

    @Test
    public void fetchAllSprint() throws Exception {
        mockMvc.perform(get("/sprint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testUpdateSprint() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Role");
        projectDTO.setStartDate(LocalDateTime.parse("2018-12-30T19:34:50.63"));
        projectDTO.setEndDate(LocalDateTime.parse("2019-12-30T19:34:50.63"));
        projectDTO = projectService.saveProject(projectDTO);

        SprintDTO sprintDTO = SprintDTO.builder()
                .name("app web")
                .project(projectDTO)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(put("/sprint/{id}", sprint.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sprintDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sprint.getId())); }

    @Test
    public void testDeleteSprint() throws Exception {
        mockMvc.perform(delete("/sprint/{id}", sprint.getId()))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetTasksBySprint() throws Exception {
        Task task = Task.builder()
                .name("Add Project")
                .sprint(sprint)
                .build();
        Task task1 = Task.builder()
                .name("Delete Project")
                .sprint(sprint)
                .build();
        taskRepository.save(task);
        taskRepository.save(task1);

        mockMvc.perform(get("/sprint/tasks-sprint/{id}",sprint.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Delete Project")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Add Project")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

}
