package com.teamtrack.controller;
import com.teamtrack.dto.*;
import com.teamtrack.entity.Team;

import com.teamtrack.repository.TeamRepository;
import com.teamtrack.service.TeamService;
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
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {

    private TeamService teamService;
private TeamRepository teamRepository;
    @GetMapping
    //@PreAuthorize("hasAuthority('LIST_TEAM')")
    public  ResponseEntity<List<TeamDTO>> getAllTeams() {
        log.info("Fetching all Teams");
        return ResponseEntity.ok(this.teamService.getAllTeams());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Void> checkTeamExists(@PathVariable Long id) {
        boolean exists = teamRepository.existsById(id);
        if (exists) {
            return ResponseEntity.ok().build(); // retourne un 200 si trouvé
        } else {
            return ResponseEntity.notFound().build(); // retourne un 404 sinon
        }
    }

    @GetMapping("/find-team/{id}")
    public  ResponseEntity<TeamDTO> getTeam(@PathVariable Long id) {
        log.info("Fetching Team");
        return ResponseEntity.ok(teamService.getTeam(id));
    }
    @GetMapping("/user/{userId}")
    public List<Long> getTeamIdsByUserId(@PathVariable Long userId) {
        return (teamService.getAllUserForTeam(userId));
    }

    @PostMapping
   // @PreAuthorize("hasAuthority('ADD_TEAM')")
    public ResponseEntity<TeamDTO> saveTeam(@Valid @RequestBody TeamDTO teamDTO) {
        log.info("Attempting to save Team");
        return new ResponseEntity<>(teamService.saveTeam(teamDTO), HttpStatus.CREATED);
    }

    /*  @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_TEAM')")
    public ResponseEntity<Team> updateTeam( @PathVariable Long id,@Valid @RequestBody TeamDTO updatedTeam) {
        log.info("Updating Team: {} for ID: {}", updatedTeam, id);
        return  ResponseEntity.ok(teamService.updateTeam(id, updatedTeam));

    }

  @GetMapping("/projects-team/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ProjectDTO>> getProjectsTeam(@PathVariable Long id) {
        log.info("Fetching projects for Team with ID: {}", id);
        return  ResponseEntity.ok(teamService.getAllProjectForTeam(id));
    }*/



   @DeleteMapping("/{id}")
   @PreAuthorize("hasAuthority('DELETE_TEAM')")
   public void deleteTeam(@PathVariable Long id) {
       log.info("Deleting Team with ID: {}", id);
       teamService.deleteTeam(id);
  }


}
