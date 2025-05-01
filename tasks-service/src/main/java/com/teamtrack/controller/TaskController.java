package com.teamtrack.controller;

import com.teamtrack.dto.TaskDTO;
import com.teamtrack.entity.Task;
import com.teamtrack.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;

    @GetMapping
   // @PreAuthorize("hasAuthority('LIST_TASK')")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("Fetching all Task");
        return ResponseEntity.ok(this.taskService.getAllTasks());
    }

    @GetMapping("/find-task/{id}")
   // @PreAuthorize("hasAuthority('VIEW_TASK')")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.info("Fetching Task");
        return ResponseEntity.ok(taskService.getTask(id));
    }

    /*@GetMapping("/comments-task/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CommentDTO>> getCommentsUser(@PathVariable Long id) {
        log.info("Fetching Comments for Task with ID: {}", id);
        return ResponseEntity.ok(taskService.getAllCommentsTask(id));
    }*/

    @PostMapping
  //  @PreAuthorize("hasAuthority('ADD_TASK')")
    public ResponseEntity<TaskDTO> saveTasks(@Valid @RequestBody TaskDTO Tasks) {
        log.info("Attempting to save Tasks");
        return new ResponseEntity<>(taskService.saveTasks(Tasks), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_TASK')")
    public ResponseEntity<Task> updateTasks( @PathVariable Long id,@Valid @RequestBody TaskDTO updateTasks) {
        log.info("Updating Task: {} for ID: {}", updateTasks, id);
            return  ResponseEntity.ok(taskService.updateTasks(id,updateTasks));
    }

    @PutMapping("/state/{id}")
   // @PreAuthorize("hasAuthority('EDIT_TASK')")
    public ResponseEntity<Task> updateStateTasks( @PathVariable Long id,@Valid @RequestBody TaskDTO updateStateTasks) {
        log.info("Updating state Task: {} for ID: {}", updateStateTasks, id);
        return  ResponseEntity.ok(taskService.updateStateTasks(id,updateStateTasks));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<Task>> getTasksBySprint(@PathVariable Long sprintId) {
        return ResponseEntity.ok(taskService.getTasksBySprintId(sprintId));
    }
    @DeleteMapping("/{id}")
   // @PreAuthorize("hasAuthority('DELETE_TASK')")
    public void deleteTask(@PathVariable Long id) {
        log.info("Deleting Task with ID: {}", id);
        taskService.deleteTask(id);

    }


}
