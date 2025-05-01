package com.teamtrack.servicestest;


import com.teamtrack.TeamtrackApplication;
import com.teamtrack.dto.*;
import com.teamtrack.dto.SprintDTO;
import com.teamtrack.dto.ProjectDTO;
import com.teamtrack.entity.*;
import com.teamtrack.entity.Sprint;
import com.teamtrack.repository.SprintRepository;
import com.teamtrack.service.SprintService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class SprintServiceTest {
    @InjectMocks
    private SprintService sprintService;
    @Mock
    private SprintRepository sprintRepository;
    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnSprint() {
        ProjectDTO projectDTO = new ProjectDTO();
        Project project = new Project();
        SprintDTO sprintDTO = SprintDTO.builder()
                .id(1L)
                .description("")
                .name("")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))  // Provide a valid start date and time
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))   // Provide a valid end date and time
                .project(projectDTO)
                .build();

        Sprint sprint = Sprint.builder()
                .id(1L)
                .project(project)
                .description("")
                .name("")  // Ensure this matches the SprintDTO field
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .deleted(false)
                .build();

        // Mock the repository behavior
        when(sprintRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(sprint));

        // Call the method under test
        SprintDTO found = sprintService.getSprint(1L);

        // Verify interactions and assertions
        assertNotNull(found, "Found task should not be null");
        assertThat(found.getName()).isEqualTo(sprintDTO.getName());
        assertThat(found.isDeleted()).isFalse();

        // Verify that the repository method was called with the correct argument
        verify(sprintRepository, times(1)).findByIdAndDeletedFalse(1L);
    }
    @Test
    public void shouldSaveSprintSuccessfully() {
        ProjectDTO projectDTO = new ProjectDTO();
        SprintDTO sprintDTO = SprintDTO.builder()
                .project(projectDTO)
                .description("test")
                .name("test")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 7, 1, 5, 0))  // Provide a valid start date and time
                .endDate(LocalDateTime.of(2024, 7, 31, 7, 0))
                .build();

        // Prepare entity corresponding to DTO
        Project project = new Project();
        Sprint sprint = Sprint.builder()
                .project(project)
                .description("test")
                .name("test")  // Ensure this matches the SprintDTO field
                .startDate(LocalDateTime.of(2024, 7, 1, 5, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 7, 0))
                .deleted(false)
                .build();

        // Mock repository behavior
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        // Call the service method
        SprintDTO savedSprint = sprintService.saveSprint(sprintDTO);

        // Verify interactions and assertions
        assertNotNull(savedSprint, "Saved sprint should not be null");
        assertEquals(sprintDTO.getName(), savedSprint.getName(), "Sprint name should match");
        assertEquals(sprintDTO.getDescription(), savedSprint.getDescription(), "Sprint description should match");
        assertEquals(sprintDTO.getStartDate(), savedSprint.getStartDate(), "Sprint start date should match");
        assertEquals(sprintDTO.getEndDate(), savedSprint.getEndDate(), "Sprint end date should match");
        assertEquals(sprintDTO.getProject(), savedSprint.getProject(), "Sprint sprint should match");

    }
    @Test
    public void shouldWhenGetAllSprint_thenReturnSprintList() {
        ProjectDTO projectDTO = new ProjectDTO();
        Project project = new Project();

        SprintDTO sprintDTO1 = SprintDTO.builder()
                .id(1L)
                .project(projectDTO)
                .description("")
                .name("Task 1")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .build();

        SprintDTO sprintDTO2 = SprintDTO.builder()
                .id(2L)
                .project(projectDTO)
                .description("")
                .name("Task 2")  // Ensure the field name matches the one in your DTO
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .build();

        Sprint taskEntity1 = Sprint.builder()
                .id(1L)
                .project(project)
                .description("")
                .name("Sprint 1")  // Ensure this matches the SprintDTO field
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .deleted(false)
                .build();

        Sprint taskEntity2 = Sprint.builder()
                .id(2L)
                .project(project)
                .description("")
                .name("Sprint 2")  // Ensure this matches the SprintDTO field
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .deleted(false)
                .build();

        // Mock the repository method to return a list of sprint
        when(sprintRepository.findByDeletedFalse()).thenReturn(Arrays.asList(taskEntity1, taskEntity2));

        // Call the method under test
        List<SprintDTO> sprintDTOS = sprintService.getAllSprints(sprintDTO1.getProject().getId());

        // Verify interactions and assertions
        assertThat(sprintDTOS).hasSize(2);
        assertThat(sprintDTOS).extracting(SprintDTO::getName).containsExactlyInAnyOrder("Sprint 1", "Sprint 2");

        // Verify that the repository method was called once
        verify(sprintRepository, times(1)).findByDeletedFalse();
    }
    @Test
    public void whenDeleteSprintThenSprintIsDeleted() {
        Long sprintId = 4L;
        Sprint sprint = new Sprint();
        sprint.setId(sprintId);
        sprint.setDeleted(false);

        // when
        when(sprintRepository.findByIdAndDeletedFalse(sprintId)).thenReturn(Optional.of(sprint));
        doAnswer(invocation -> {
            Sprint savedSprint1 = invocation.getArgument(0);
            savedSprint1.setDeleted(true);
            return null;
        }).when(sprintRepository).save(any(Sprint.class));

        // Call the service method that should invoke deleteById
        sprintService.deleteSprint(sprintId);

        // then
        assertThat(sprint.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(sprintRepository).save(sprint);  // Verify that the save method was called
    }
    @Test
    public void testUpdateSprintSuccessfully() {
        Long sprintId = 1L;
        ProjectDTO ProjectDTO = new ProjectDTO();
        RoleDTO roleDTO = new RoleDTO();
        SprintDTO sprintDTO = new SprintDTO();

        Project Project = new Project();
        Sprint sprint = new Sprint();
        Sprint taskEntity2 = Sprint.builder()
                .id(2L)
                .project(Project)
                .description("test")
                .name("Task 2")  // Ensure this matches the SprintDTO field
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .deleted(false)
                .build();
        SprintDTO updatedSprintDTO = new SprintDTO();
        updatedSprintDTO.setName("New Name");
        updatedSprintDTO.setDescription("test4");
        updatedSprintDTO.setEndDate(LocalDateTime.of(2024, 8, 1, 9, 0));
        updatedSprintDTO.setStartDate(LocalDateTime.of(2024, 8, 31, 17, 0));
        updatedSprintDTO.setProject(ProjectDTO);

        when(sprintRepository.findByIdAndDeletedFalse(taskEntity2.getId())).thenReturn(Optional.of(taskEntity2));
        when(sprintRepository.save(taskEntity2)).thenReturn(taskEntity2);

        Sprint updatedSprint = sprintService.updateSprint(taskEntity2.getId(),updatedSprintDTO);


        assertEquals(updatedSprintDTO.getName(), updatedSprint.getName());
        assertEquals(updatedSprintDTO.getDescription(), updatedSprint.getDescription());
        assertEquals(updatedSprintDTO.getEndDate(), updatedSprint.getEndDate());
        assertEquals(updatedSprintDTO.getStartDate(), updatedSprint.getStartDate());
    }
    @Test
    public void testUpdateSprintNotFound() {
        Long sprintId = 1L;
        SprintDTO updatedSprintDTO = new SprintDTO();

        when(sprintRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sprintService.updateSprint(sprintId, updatedSprintDTO);
        });

        assertEquals("Sprint with not found", exception.getMessage());
    }
    @Test
    public void testSaveSprint_InvalidId_ThrowsException() {
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.setId(1L); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.saveSprint(sprintDTO);
        });
        assertEquals("Sprint ID must be null or 0 for new users.", exception.getMessage());

    }

    @Test
    public void testSaveSprint_StartDateAfterEndDate_ThrowsException() {
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.setStartDate(LocalDateTime.of(2024, 7, 20, 9, 0));
        sprintDTO.setEndDate(LocalDateTime.of(2023, 7, 19, 17, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sprintService.saveSprint(sprintDTO);
        });

        assertEquals("Start date cannot be after end date.", exception.getMessage());
    }
    @Test
    public void testSaveSprint_ValidDates_Success() {
        SprintDTO sprintDTO = new SprintDTO();
        sprintDTO.setStartDate(LocalDateTime.of(2024, 7, 19, 9, 0));
        sprintDTO.setEndDate(LocalDateTime.of(2024, 7, 20, 17, 0));
        Sprint sprint = new Sprint();
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        SprintDTO result = sprintService.saveSprint(sprintDTO);

        assertNotNull(result);
        verify(sprintRepository, times(1)).save(any(Sprint.class));
    }

}
