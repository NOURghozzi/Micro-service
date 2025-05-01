package com.teamtrack.controller;
import com.teamtrack.dto.ProjectDTO;
import com.teamtrack.entity.Project;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.service.ProjectService;
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
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
   // @PreAuthorize("hasAuthority('LIST_PROJECT')")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@RequestHeader("Authorization") String token) {
        log.info("Fetching all Project");
        return ResponseEntity.ok(this.projectService.getAllProjects());
    }

    @GetMapping("/find-project/{id}")
   // @PreAuthorize("hasAuthority('VIEW_PROJECT')")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        log.info("Fetching Team");
        return ResponseEntity.ok(projectService.getProject(id));
    }

   /* @GetMapping("/sprints-project/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<int>> getSprintsProject(@PathVariable Long id)  {
            log.info("Fetching Sprints for Project with ID :{}",id);
        return ResponseEntity.ok(projectService.getAllSprintForProject(id));
    }*/

    @PostMapping
   // @PreAuthorize("hasAuthority('ADD_PROJECT')")
    public ResponseEntity<ProjectDTO> saveProject(@Valid @RequestBody ProjectDTO Project) {
        log.info("Attempting to save Project");
        return new ResponseEntity<>(projectService.saveProject(Project), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
   // @PreAuthorize("hasAuthority('EDIT_PROJECT')")
    public ResponseEntity<Project> updateProject( @PathVariable Long id,@Valid @RequestBody ProjectDTO updatedProject)  {
        log.info("Updating Project: {} for ID: {}", updatedProject, id);
            return  ResponseEntity.ok(projectService.updateProject(id, updatedProject));
    }

    @DeleteMapping("/{id}")
   // @PreAuthorize("hasAuthority('DELETE_PROJECT')")
    public void deleteProject(@PathVariable Long id) {
       log.info("Deleting Project with ID: {}", id);
        projectService.deleteProject(id);

   }


}
