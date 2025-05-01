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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TeamControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TeamService teamService;
    Team team;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        User user = User.builder().name("nour")
                .build();

        user  = userRepository.save(user);
        team= Team.builder()
                .name("NO ACTIVE")
                .users(List.of(user))
                .deleted(false)
                .build();
        team = teamRepository.save(team);

    }

    @Test
    public void testAddTeam() throws Exception {
        TeamDTO teamDTO = TeamDTO.builder()
                .name("app web")
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(teamDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void fetchTeamById() throws Exception {
        mockMvc.perform(get("/team/find-team/" +team.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assert(teamRepository.findByIdAndDeletedFalse(team.getId()).isPresent());
    }

    @Test
    public void fetchAllTeam() throws Exception {
        mockMvc.perform(get("/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testUpdateTeam() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO = roleService.saveRole(roleDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("gestion de projet");
        userDTO.setEmail("nour@gmail.com");
        userDTO.setRole(roleDTO);
        userDTO = userService.saveUser(userDTO);

        TeamDTO teamDTO = TeamDTO.builder()
                .name("app web")
                .users(List.of(userDTO))
                .deleted(false)
                .build();
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/team/{id}", team.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(teamDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(team.getId())); }

    @Test
    public void testDeleteTeam() throws Exception {
        mockMvc.perform(delete("/team/{id}", team.getId()))
                .andExpect(status().isOk()) // Modifié pour vérifier que le statut est 200 OK
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetUsersByTeam() throws Exception {
        Role role =  Role.builder().name("Admin").build();
        role = roleRepository.save(role);

        User user = User.builder()
                .name("nour")
                .role(role)
                .email("nourghozzi9@gmail.com")
                .build();

        User user1 = User.builder()
                .name("nounou")
                .role(role)
                .email("nourghozzi@gmail.com")
                .build();

        userRepository.save(user);
        userRepository.save(user1);
        team.setUsers(List.of(user1,user));
        team = teamRepository.save(team);
        mockMvc.perform(get("/team/user-team/{id}",team.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("nounou")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("nour")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

    @Test
    public void testGetProjectsByTeam() throws Exception {
        Project project = Project.builder()
                .name("app web")
                .team(team)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();
        Project project1 = Project.builder()
                .name("app Mobile")
                .team(team)
                .description("app web spring-boot angular")
                .startDate(LocalDateTime.parse("2018-12-30T19:34:50.63"))
                .endDate(LocalDateTime.parse("2019-12-30T19:34:50.63"))
                .deleted(false)
                .build();
        projectRepository.save(project1);
        projectRepository.save(project);

        mockMvc.perform(get("/team/projects-team/{id}",team.getId()))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Vérifie le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("app Mobile")) // Vérifie le premier élément
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("app web")) // Vérifie le deuxième élément
                .andDo(MockMvcResultHandlers.print()); // Affiche les détails de la réponse
    }

}
