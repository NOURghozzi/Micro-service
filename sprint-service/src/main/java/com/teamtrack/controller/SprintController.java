package com.teamtrack.controller;
import com.teamtrack.dto.SprintDTO;
import com.teamtrack.entity.Sprint;
import com.teamtrack.service.SprintService;
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
@RequestMapping("/sprint")
@AllArgsConstructor
public class SprintController {

    private SprintService sprintService;


    @GetMapping("/{id}")
   // @PreAuthorize("hasAuthority('LIST_SPRINT')")
    public ResponseEntity<List<SprintDTO>> getAllSprints(@PathVariable Long id) {
        log.info("Fetching all Sprint For Project id "+id);
        return ResponseEntity.ok(this.sprintService.getAllSprints(id));
    }

    @GetMapping("/find-sprint/{id}")
   // @PreAuthorize("hasAuthority('VIEW_SPRINT')")
    public ResponseEntity<SprintDTO> getSprint(@PathVariable Long id) {
        log.info("Fetching Sprint");
        return ResponseEntity.ok(sprintService.getSprint(id));
    }

    @GetMapping("/project-sprints/{id}")
    public ResponseEntity<List<SprintDTO>> getProjectIdSprint(@PathVariable Long id) {
        log.info("Fetching Tasks for Sprint with ID: {}", id);
        return ResponseEntity.ok(sprintService.getAllSprintsByProject(id));}

    @PutMapping("/{id}")
  //  @PreAuthorize("hasAuthority('EDIT_SPRINT')")
    public ResponseEntity<Sprint> updateSprint( @PathVariable Long id,@Valid @RequestBody SprintDTO updatedSprint) {
        log.info("Updating Sprint: {} for ID: {}", updatedSprint, id);
            return  ResponseEntity.ok(sprintService.updateSprint(id,updatedSprint));

    }

    @PutMapping("/state/{id}")
 //   @PreAuthorize("hasAuthority('EDIT_SPRINT')")
    public ResponseEntity<Sprint> updateStateSprints(@PathVariable Long id, @Valid @RequestBody SprintDTO updateStateSprints) {
        log.info("Updating state sprint: {} for ID: {}", updateStateSprints, id);
        return  ResponseEntity.ok(sprintService.updateStateSprints(id,updateStateSprints));
    }

    @PostMapping
  //  @PreAuthorize("hasAuthority('ADD_SPRINT')")
    public ResponseEntity<SprintDTO> saveSprint(@Valid @RequestBody SprintDTO Sprint) {
        log.info("Attempting to save Sprint");
        return new ResponseEntity<>(sprintService.saveSprint(Sprint), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
  //  @PreAuthorize("hasAuthority('DELETE_SPRINT')")
    public void deleteSprint(@PathVariable Long id) {
        log.info("Deleting Sprint with ID: {}", id);
        sprintService.deleteSprint(id);

    }


}