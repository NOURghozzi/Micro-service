package com.teamtrack.service;

import com.teamtrack.Feign.UserClient;
import com.teamtrack.converter.TeamMapper;
import com.teamtrack.dto.TeamDTO;
import com.teamtrack.entity.Team;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.repository.TeamRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class TeamService {

    private TeamRepository teamRepository;
    private HttpServletRequest request;

    private UserClient uesrClient ;

    public TeamDTO getTeam(Long id) {

        log.info("Fetching team");

        return TeamMapper.modelToDto(teamRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("team does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

   /* public List<TeamDTO> getAllTeams() {

        log.info("Fetching all teams");
        String userId = request.getHeader("id");
        String role = request.getHeader("role");
        if (role.equals("ADMIN") ||role.equals("MANAGER")) {
            List<Team> teams = teamRepository.findByDeletedFalse();
            return teams.stream().map(TeamMapper::modelToDto).collect(Collectors.toList());
        }else {
            List<Team> teams = teamRepository.findAllByUserIdsContainsAndDeletedFalse(Long.valueOf(userId));
            return teams.stream().map(TeamMapper::modelToDto).collect(Collectors.toList());
        }
       }
*/
   public List<TeamDTO> getAllTeams() {
       log.info("Fetching all teams");

       // Récupérer le rôle de l'utilisateur à partir du SecurityContext
       String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

       if (role.contains("ADMIN") || role.contains("MANAGER")) {
           List<Team> teams = teamRepository.findByDeletedFalse();
           return teams.stream().map(TeamMapper::modelToDto).collect(Collectors.toList());

       } else {
           System.out.println("no");
       }
       return null;
   }



    public List<Long> getAllUserForTeam(Long id) {
        log.info("Fetching users for team with ID: {}", id);
        Team team = teamRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("team does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        return team.getUserIds();
    }

    public TeamDTO saveTeam(TeamDTO teamDTO) {

        log.info("Saving Team: {}", teamDTO);

        if ( (teamDTO.getId() != null)) {
            throw new HttpCustomException("Team ID must be null or 0 for new users.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());}
        teamRepository.findByNameAndDeletedFalse(teamDTO.getName())
                .ifPresent((test) -> {
                    throw new HttpCustomException("The Name " + test.getName() + " already exists.", HttpStatus.CONFLICT, HttpStatus.CONFLICT.value());
                });
        for (Long userId : teamDTO.getUserIds()) {
            if (!checkUserExists(userId)) {
                throw new HttpCustomException("User ID " + userId + " not found.", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
            }
        }
        return TeamMapper.modelToDto(teamRepository.save(TeamMapper.dtoToModel(teamDTO)));
    }
    public boolean checkUserExists(Long teamId) {
        try {
            uesrClient.getUser(teamId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Erreur lors de la vérification du team : " + e.getMessage());
            return false;
        }
    }
   /* public Team updateTeam(Long id, TeamDTO updatedTeam) {
        log.info("Updating Team: {} for ID: {}", updatedTeam, id);
        Team existingTeam = teamRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("team does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
            existingTeam.setName(updatedTeam.getName());
        existingTeam.setName(updatedTeam.getName());
        existingTeam.setSpeciality(SpecialityMapper.dtoToModel(updatedTeam.getSpeciality()));
        existingTeam.setUsers(
                updatedTeam.getUsers().stream()
                        .map(UserMapper::dtoToModel)
                        .collect(Collectors.toList())
        );            return teamRepository.save(existingTeam);

    }*/

    public void deleteTeam(Long id) {
      log.info("Deleting Team with ID: {}", id);
      Team team = teamRepository.findByIdAndDeletedFalse(id)
              .orElseThrow(() -> new HttpCustomException("team does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
      team.setDeleted(true);
      teamRepository.save(team);
  }

}
