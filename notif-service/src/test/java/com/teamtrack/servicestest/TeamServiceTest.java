package com.teamtrack.servicestest;

import com.teamtrack.TeamtrackApplication;
import com.teamtrack.dto.*;
import com.teamtrack.dto.TeamDTO;
import com.teamtrack.entity.*;
import com.teamtrack.entity.Team;
import com.teamtrack.repository.TeamRepository;
import com.teamtrack.service.TeamService;
import org.junit.Test;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class TeamServiceTest {
    @InjectMocks
    private TeamService teamService;
    @Mock
    private TeamRepository teamRepository;
    public TeamServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldSaveTeamSuccessfully() {
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOS = List.of(userDTO);
        TeamDTO teamDTO = TeamDTO.builder()
                .name("test")  // Ensure the field name matches the one in your DTO
                .users(userDTOS)
                .build();

        // Prepare entity corresponding to DTO
        User user = new User();
        List<User> users = List.of(user);

        Team team = Team.builder()
                .name("test")  // Ensure the field name matches the one in your DTO
                .users(users)
                .build();
        // Mock repository behavior
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // Call the service method
        TeamDTO savedTeam = teamService.saveTeam(teamDTO);

        // Verify interactions and assertions
        assertNotNull(savedTeam, "Saved task should not be null");
        assertEquals(teamDTO.getName(), savedTeam.getName(), "Task name should match");
        assertEquals(teamDTO.getUsers(), savedTeam.getUsers(), "Task user should match");

   }
    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnTeam() {
        UserDTO userDTO = new UserDTO();
        List<UserDTO> userDTOS = List.of(userDTO);
        User user = new User();
        List<User> users = List.of(user);
        TeamDTO teamDTO = TeamDTO.builder()
                .id(1L)
                .users(userDTOS)
                .name("")  // Ensure the field name matches the one in your DTO
               .build();

        Team taskEntity = Team.builder()
                .id(1L)
                .users(users)
                .name("")  // Ensure this matches the TeamDTO field
                .build();

        // Mock the repository behavior
        when(teamRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(taskEntity));

        // Call the method under test
        TeamDTO foundTask = teamService.getTeam(1L);

        // Verify interactions and assertions
        assertNotNull(foundTask, "Found team should not be null");
        assertThat(foundTask.getName()).isEqualTo(teamDTO.getName());
        assertThat(foundTask.isDeleted()).isFalse();

        // Verify that the repository method was called with the correct argument
        verify(teamRepository, times(1)).findByIdAndDeletedFalse(1L);
    }
    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnTeamList() {
        User user = new User();
        List<User> users = List.of(user);

        Team taskEntity = Team.builder()
                .id(1L)
                .users(users)
                .name("1")  // Ensure this matches the TeamDTO field
                .build();
        Team taskEntity1 = Team.builder()
                .id(2L)
                .users(users)
                .name("2")  // Ensure this matches the TeamDTO field
                .build();

        // Mock the repository behavior
        when(teamRepository.findByDeletedFalse()).thenReturn(Arrays.asList(taskEntity, taskEntity1));

        // Call the method under test
        List<TeamDTO> teamDTOS = teamService.getAllTeams();


        // Verify interactions and assertions
        assertThat(teamDTOS).hasSize(2);
        assertThat(teamDTOS).extracting(TeamDTO::getName).containsExactlyInAnyOrder("1", "2");

        // Verify that the repository method was called once
        verify(teamRepository, times(1)).findByDeletedFalse();

    }
    @Test
    public void whenDeleteTeamThenTeamIsDeleted() {
        // given
        Long teamId = 4L;
        Team team = new Team();
        team.setId(teamId);
        team.setDeleted(false);

        // when
        when(teamRepository.findByIdAndDeletedFalse(teamId)).thenReturn(Optional.of(team));
        doAnswer(invocation -> {
            Team savedTeam = invocation.getArgument(0);
            savedTeam.setDeleted(true);
            return null;
        }).when(teamRepository).save(any(Team.class));

        // Call the service method that should invoke deleteById
        teamService.deleteTeam(teamId);

        // then
        assertThat(team.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(teamRepository).save(team);  // Verify that the save method was called


    }
    @Test
    public void testUpdateTeamSuccessfully() {
        UserDTO userDTO = new UserDTO();
        RoleDTO roleDTO = new RoleDTO();
        userDTO.setRole(roleDTO);

        User user = new User();
        Role role = new Role();
        user.setRole(role);

        List<User> users = List.of(user);
        List<UserDTO> userDTOS  = List.of(userDTO);

        Team team = Team.builder()
                .id(1L)
                .users(users)
                .name("1")  // Ensure this matches the TeamDTO field
                .build();
        TeamDTO updatedTeamDTO = new TeamDTO();
        updatedTeamDTO.setName("New Name");
        updatedTeamDTO.setUsers(userDTOS);
        when(teamRepository.findByIdAndDeletedFalse(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);

        Team updatedTeam = teamService.updateTeam(team.getId(), updatedTeamDTO);


        assertEquals(updatedTeamDTO.getName(), updatedTeam.getName());
          }
    @Test
    public void testUpdateTeamNotFound() {
        int TeamId = 1;
        TeamDTO updatedTeamDTO = new TeamDTO();

        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            teamService.updateTeam((long) TeamId, updatedTeamDTO);
        });

        assertEquals("Team not found", exception.getMessage());
    }

    @Test
    public void testSaveTeam_InvalidId_ThrowsException() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(1L); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            teamService.saveTeam(teamDTO);
        });
        assertEquals("Team ID must be null or 0 for new users.", exception.getMessage());

    }

}
