package com.teamtrack.servicestest;

import com.teamtrack.TeamtrackApplication;
import com.teamtrack.dto.*;
import com.teamtrack.entity.*;
import com.teamtrack.entity.Team;
import com.teamtrack.repository.ProjectRepository;
import com.teamtrack.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    public ProjectServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldSaveProjectSuccessfully() {
        TeamDTO teamDTO = new TeamDTO();
        ProjectDTO projectDTO = ProjectDTO.builder()
                .team(teamDTO)
                .description("test")
                .name("test")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 7, 1, 5, 0))  // Provide a valid start date and time
                .endDate(LocalDateTime.of(2024, 7, 31, 7, 0))
                .build();

        // Prepare entity corresponding to DTO
        Team team = new Team();
        Project project = Project.builder()
                .team(team)
                .description("test")
                .name("test")  // Ensure this matches the EquipeDTO field
                .startDate(LocalDateTime.of(2024, 7, 1, 5, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 7, 0))
                .deleted(false)
                .build();

        // Mock repository behavior
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Call the service method
        ProjectDTO savedProject = projectService.saveProject(projectDTO);

        // Verify interactions and assertions
        assertNotNull(savedProject, "Saved Project should not be null");
        assertEquals(projectDTO.getName(), savedProject.getName(), "Project name should match");
        assertEquals(projectDTO.getDescription(), savedProject.getDescription(), "Project description should match");
        assertEquals(projectDTO.getStartDate(), savedProject.getStartDate(), "Project start date should match");
        assertEquals(projectDTO.getEndDate(), savedProject.getEndDate(), "Project end date should match");
        assertEquals(projectDTO.getTeam(), savedProject.getTeam(), "Project  should match");

    }

    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnProject() {
        TeamDTO teamDTO = new TeamDTO();
        Team team = new Team();
        ProjectDTO projectDTO = ProjectDTO.builder()
                .id(1L)
                .team(teamDTO)
                .description("")
                .name("")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))  // Provide a valid start date and time
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))   // Provide a valid end date and time
                .build();

        Project project = Project.builder()
                .id(1L)
                .team(team)
                .description("")
                .name("")  // Ensure this matches the EquipeDTO field
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .deleted(false)
                .build();

        // Mock the repository behavior
        when(projectRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(project));

        // Call the method under test
        ProjectDTO found = projectService.getProject(1L);

        // Verify interactions and assertions
        assertNotNull(found, "Found Project should not be null");
        assertThat(found.getName()).isEqualTo(projectDTO.getName());
        assertThat(found.isDeleted()).isFalse();

        // Verify that the repository method was called with the correct argument
        verify(projectRepository, times(1)).findByIdAndDeletedFalse(1L);
    }
    @Test
    public void shouldWhenGetAllProject_thenReturnProjectList() {
        TeamDTO teamDTO = new TeamDTO();
        Team team = new Team();
        ProjectDTO projectDTO1 = ProjectDTO.builder()
                .id(1L)
                .team(teamDTO)
                .description("")
                .name("Project 1")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .build();

        ProjectDTO projectDTO2 = ProjectDTO.builder()
                .id(2L)
                .team(teamDTO)
                .description("")
                .name("Project 2")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .build();

        Project ProjectEntity1 = Project.builder()
                .id(1L)
                .team(team)
                .description("")
                .name("Project 1")  // Ensure this matches the ProjectDTO field
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .deleted(false)
                .build();

        Project ProjectEntity2 = Project.builder()
                .id(2L)
                .team(team)
                .description("")
                .name("Project 2")  // Ensure this matches the ProjectDTO field
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .deleted(false)
                .build();

        // Mock the repository method to return a list of project
        when(projectRepository.findByDeletedFalse()).thenReturn(Arrays.asList(ProjectEntity1, ProjectEntity2));

        // Call the method under test
        List<ProjectDTO> projectDTOS = projectService.getAllProjects();

        // Verify interactions and assertions
        assertThat(projectDTOS).hasSize(2);
        assertThat(projectDTOS).extracting(ProjectDTO::getName).containsExactlyInAnyOrder("Project 1", "Project 2");

        // Verify that the repository method was called once
        verify(projectRepository, times(1)).findByDeletedFalse();
    }
    @Test
    public void whenDeleteProjectThenProjectIsDeleted() {
        Long projectId = 4L;
        Project project = new Project();
        project.setId(projectId);
        project.setDeleted(false);

        // when
        when(projectRepository.findByIdAndDeletedFalse(projectId)).thenReturn(Optional.of(project));
        doAnswer(invocation -> {
            Project savedProject = invocation.getArgument(0);
            savedProject.setDeleted(true);
            return null;
        }).when(projectRepository).save(any(Project.class));

        // Call the service method that should invoke deleteById
        projectService.deleteProject(projectId);

        // then
        assertThat(project.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(projectRepository).save(project);  // Verify that the save method was called
    }
    @Test
    public void testUpdateProjectSuccess() {
        Long projectId = 1L;
        TeamDTO teamDTO = new TeamDTO();
        Team team = new Team();
        Project project = Project.builder()
                .id(projectId)
                .team(team)
                .description("test")
                .name("Project 2")  // Ensure this matches the ProjectDTO field
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .deleted(false)
                .build();
        ProjectDTO updatedProjectDTO = new ProjectDTO();
        updatedProjectDTO.setName("New Name");
        updatedProjectDTO.setDescription("test4");
        updatedProjectDTO.setEndDate(LocalDateTime.of(2024, 8, 1, 9, 0));
        updatedProjectDTO.setStartDate(LocalDateTime.of(2024, 8, 31, 17, 0));
        updatedProjectDTO.setTeam(teamDTO);

        when(projectRepository.findByIdAndDeletedFalse(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        Project updatedProject = projectService.updateProject(project.getId(),updatedProjectDTO);


        assertEquals(updatedProjectDTO.getName(), updatedProject.getName());
        assertEquals(updatedProjectDTO.getDescription(), updatedProject.getDescription());
        assertEquals(updatedProjectDTO.getEndDate(), updatedProject.getEndDate());
        assertEquals(updatedProjectDTO.getStartDate(), updatedProject.getStartDate());
    }
    @Test
    public void testUpdateProjectNotFound() {
        Long projectId = 1L;
        ProjectDTO updatedProjectDTO = new ProjectDTO();

        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(projectId, updatedProjectDTO);
        });

        assertEquals("Project  not found", exception.getMessage());
    }
    @Test
    public void testSaveProject_InvalidId_ThrowsException() {
        ProjectDTO projectsDTO = new ProjectDTO();
        projectsDTO.setId(1L); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.saveProject(projectsDTO);
        });
        assertEquals("Project ID must be null or 0 for new users.", exception.getMessage());

    }

    @Test
    public void testSaveProject_StartDateAfterEndDate_ThrowsException() {
        ProjectDTO projectsDTO = new ProjectDTO();
        projectsDTO.setStartDate(LocalDateTime.of(2024, 7, 20, 9, 0));
        projectsDTO.setEndDate(LocalDateTime.of(2023, 7, 19, 17, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.saveProject(projectsDTO);
        });

        assertEquals("Start date cannot be after end date.", exception.getMessage());
    }
    @Test
    public void testSaveProject_ValidDates_Success() {
        ProjectDTO projectsDTO = new ProjectDTO();
        projectsDTO.setStartDate(LocalDateTime.of(2024, 7, 19, 9, 0));
        projectsDTO.setEndDate(LocalDateTime.of(2024, 7, 20, 17, 0));
        Project projects = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(projects);

        ProjectDTO result = projectService.saveProject(projectsDTO);

        assertNotNull(result);
        verify(projectRepository,times(1)).save(any(Project.class));
    }

}
