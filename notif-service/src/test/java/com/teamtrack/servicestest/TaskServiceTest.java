package com.teamtrack.servicestest;

import com.teamtrack.TeamtrackApplication;
import com.teamtrack.dto.*;
import com.teamtrack.entity.*;
import com.teamtrack.repository.TaskRepository;
import com.teamtrack.service.TaskService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldSaveTaskSuccessfully() {
        UserDTO userDTO = new UserDTO();
        SprintDTO sprintDTO = new SprintDTO();
        TaskDTO taskDTO = TaskDTO.builder()
                .user(userDTO)
                .description("test")
                .name("test")  // Ensure the field name matches the one in your DTO
                .sprint(sprintDTO)
                .startDate(LocalDateTime.of(2024, 7, 1, 5, 0))  // Provide a valid start date and time
                .endDate(LocalDateTime.of(2024, 7, 31, 7, 0))
                .build();

        // Prepare entity corresponding to DTO
        User user = new User();
        Sprint sprint = new Sprint();
        Task taskEntity = Task.builder()
                .user(user)
                .description("test")
                .name("test")  // Ensure this matches the TasksDTO field
                .sprint(sprint)
                .startDate(LocalDateTime.of(2024, 7, 1, 5, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 7, 0))
                .deleted(false)
                .build();

        // Mock repository behavior
        when(taskRepository.save(any(Task.class))).thenReturn(taskEntity);

        // Call the service method
        TaskDTO savedTask = taskService.saveTasks(taskDTO);

        // Verify interactions and assertions
        assertNotNull(savedTask, "Saved task should not be null");
        assertEquals(taskDTO.getName(), savedTask.getName(), "Task name should match");
        assertEquals(taskDTO.getDescription(), savedTask.getDescription(), "Task description should match");
        assertEquals(taskDTO.getStartDate(), savedTask.getStartDate(), "Task start date should match");
        assertEquals(taskDTO.getEndDate(), savedTask.getEndDate(), "Task end date should match");
        assertEquals(taskDTO.getUser(), savedTask.getUser(), "Task user should match");
        assertEquals(taskDTO.getSprint(), savedTask.getSprint(), "Task sprint should match");

        // Verify that the saveTasks method was called once
//        verify(tasksService, times(1)).saveTasks(tasksDTO);
    }

    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnTasks() {
        UserDTO userDTO = new UserDTO();
        SprintDTO sprintDTO = new SprintDTO();
        User user = new User();
        Sprint sprint = new Sprint();
        TaskDTO taskDTO = TaskDTO.builder()
                .id(1L)
                .user(userDTO)
                .description("")
                .name("")  // Ensure the field name matches the one in your DTO
                .sprint(sprintDTO)
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))  // Provide a valid start date and time
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))   // Provide a valid end date and time
                .build();

        Task taskEntity = Task.builder()
                .id(1L)
                .user(user)
                .description("")
                .name("")  // Ensure this matches the TasksDTO field
                .sprint(sprint)
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .deleted(false)
                .build();

        // Mock the repository behavior
        when(taskRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(taskEntity));

        // Call the method under test
        TaskDTO foundTask = taskService.getTask(1L);

        // Verify interactions and assertions
        assertNotNull(foundTask, "Found task should not be null");
        assertThat(foundTask.getName()).isEqualTo(taskDTO.getName());
        assertThat(foundTask.isDeleted()).isFalse();

        // Verify that the repository method was called with the correct argument
        verify(taskRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    public void shouldWhenGetAllTask_thenReturnTasksList() {
        UserDTO userDTO = new UserDTO();
        SprintDTO sprintDTO = new SprintDTO();
        User user = new User();
        Sprint sprint = new Sprint();

        TaskDTO taskDTO1 = TaskDTO.builder()
                .id(1L)
                .user(userDTO)
                .description("")
                .name("Task 1")  // Ensure the field name matches the one in your DTO
                .sprint(sprintDTO)
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .build();

        TaskDTO taskDTO2 = TaskDTO.builder()
                .id(2L)
                .user(userDTO)
                .description("")
                .name("Task 2")  // Ensure the field name matches the one in your DTO
                .sprint(sprintDTO)
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .build();

        Task taskEntity1 = Task.builder()
                .id(1L)
                .user(user)
                .description("")
                .name("Task 1")  // Ensure this matches the TasksDTO field
                .sprint(sprint)
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 7, 31, 17, 0))
                .deleted(false)
                .build();

        Task taskEntity2 = Task.builder()
                .id(2L)
                .user(user)
                .description("")
                .name("Task 2")  // Ensure this matches the TasksDTO field
                .sprint(sprint)
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .deleted(false)
                .build();

        // Mock the repository method to return a list of tasks
        when(taskRepository.findByDeletedFalse()).thenReturn(Arrays.asList(taskEntity1, taskEntity2));

        // Call the method under test
        List<TaskDTO> taskDTOS = taskService.getAllTasks();

        // Verify interactions and assertions
        assertThat(taskDTOS).hasSize(2);
        assertThat(taskDTOS).extracting(TaskDTO::getName).containsExactlyInAnyOrder("Task 1", "Task 2");

        // Verify that the repository method was called once
        verify(taskRepository, times(1)).findByDeletedFalse();
    }
    @Test
    public void whenDeleteTaskThenTasksIsDeleted() {
        // given
        Long taskId = 4L;
        Task task = new Task();
        task.setId(taskId);
        task.setDeleted(false);

        // when
        when(taskRepository.findByIdAndDeletedFalse(taskId)).thenReturn(Optional.of(task));
        doAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setDeleted(true);
            return null;
        }).when(taskRepository).save(any(Task.class));

        // Call the service method that should invoke deleteById
        taskService.deleteTask(taskId);

        // then
        assertThat(task.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(taskRepository).save(task);  // Verify that the save method was called

    }

    @Test
    public void testUpdateTaskSuccessfully() {
        UserDTO userDTO = new UserDTO();
        SprintDTO sprintDTO = new SprintDTO();
        RoleDTO roleDTO = new RoleDTO();
        userDTO.setRole(roleDTO);
        Role role = new Role();
        User user = new User();
        user.setRole(role);
        Sprint sprint = new Sprint();
        Task task = Task.builder()
                .id(2L)
                .user(user)
                .description("test")
                .name("Task 2")  // Ensure this matches the TasksDTO field
                .sprint(sprint)
                .startDate(LocalDateTime.of(2024, 8, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 8, 31, 17, 0))
                .deleted(false)
                .build();
        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setName("New Name");
        updatedTaskDTO.setDescription("New Name");
        updatedTaskDTO.setUser(userDTO);
        updatedTaskDTO.setStartDate(LocalDateTime.of(2024, 8, 1, 9, 0));
        updatedTaskDTO.setEndDate(LocalDateTime.of(2024, 8, 1, 9, 0));
        updatedTaskDTO.setSprint(sprintDTO);
        when(taskRepository.findByIdAndDeletedFalse(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = taskService.updateTasks(task.getId(), updatedTaskDTO);


        assertEquals(updatedTask.getName(), updatedTaskDTO.getName());
    }

    @Test
    public void testUpdateTasksNotFound() {
        Long taskId = 1L;
        TaskDTO updatedTaskDTO = new TaskDTO();

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTasks(taskId, updatedTaskDTO);
        });

        assertEquals("Task with not found", exception.getMessage());
    }
    @Test
    public void testSaveTask_InvalidId_ThrowsException() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.saveTasks(taskDTO);
        });
       assertEquals("Task ID must be null or 0 for new users.", exception.getMessage());

    }
    @Test
    public void testSaveTask_StartDateAfterEndDate_ThrowsException() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStartDate(LocalDateTime.of(2024, 7, 20, 9, 0));
        taskDTO.setEndDate(LocalDateTime.of(2023, 7, 19, 17, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.saveTasks(taskDTO);
        });

        assertEquals("Start date cannot be after end date.", exception.getMessage());
    }
    @Test
    public void testSaveTask_ValidDates_Success() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStartDate(LocalDateTime.of(2024, 7, 19, 9, 0));
        taskDTO.setEndDate(LocalDateTime.of(2024, 7, 20, 17, 0));
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.saveTasks(taskDTO);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
}
