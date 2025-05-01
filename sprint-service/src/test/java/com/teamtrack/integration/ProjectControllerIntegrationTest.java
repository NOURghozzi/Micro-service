package com.teamtrack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teamtrack.dto.*;
import com.teamtrack.entity.*;
import com.teamtrack.repository.*;
import com.teamtrack.service.*;
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
public class ProjectControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ProjectService projectService;

    Project project ;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Team team = Team.builder().name("Web").build();
        team = teamRepository.save(team);

        project = Project.builder()
                .name("app web")
                .team(team)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();
        project = projectRepository.save(project);
    }

    @Test
    public void testAddProject() throws Exception {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("gestion de projet");
        teamDTO = teamService.saveTeam(teamDTO);

        ProjectDTO projectDTO = ProjectDTO.builder()
                .name("Authentification")
                .team(teamDTO)
                .description("Authentification ")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void fetchProjectById() throws Exception {
        mockMvc.perform(get("/project/find-project/" +project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(projectRepository.findByIdAndDeletedFalse(project.getId()).isPresent());
    }

    @Test
    public void fetchAllProject() throws Exception {
        mockMvc.perform(get("/project")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testUpdateProject() throws Exception {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName("gestion de projet");
        teamDTO = teamService.saveTeam(teamDTO);

        ProjectDTO projectDTO = ProjectDTO.builder()
                .name("Authentification")
                .team(teamDTO)
                .description("Authentification ")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();

        mockMvc.perform(put("/project/{id}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(project.getId())); }

    @Test
    public void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/project/{id}", project.getId()))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetSprintsByProject() throws Exception {
        Sprint sprint = Sprint.builder()
                .name("Delete Project")
                .project(project)
                .build();
        Sprint sprint1 = Sprint.builder()
                .name("Add Project")
                .project(project)
                .build();
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint);

        mockMvc.perform(get("/project/sprints-project/{id}",project.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Delete Project")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Add Project")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }



}
