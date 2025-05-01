package com.teamtrack.service;

import com.teamtrack.Feign.TeamClient;
import com.teamtrack.converter.ProjectMapper;
import com.teamtrack.dto.ProjectDTO;
import com.teamtrack.entity.*;
import com.teamtrack.entity.Project;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.repository.ProjectRepository;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private HttpServletRequest request;

    private final TeamClient teamClient;


    public List<ProjectDTO> getAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getCredentials();

        Long userId = jwt.getClaim("id");
        String role = jwt.getClaim("role");

        if (role.equals("ADMIN") || role.equals("MANAGER")) {
            return projectRepository.findByDeletedFalse()
                    .stream().map(ProjectMapper::modelToDto).toList();
        } else {
            List<Long> teamIds = teamClient.getTeamIdsByUserId(userId);
            return projectRepository.findByTeamIdInAndDeletedFalse(teamIds)
                    .stream().map(ProjectMapper::modelToDto).toList();
        }
    }


    public ProjectDTO getProject(Long id)  {
        log.info("Fetching task");
        return ProjectMapper.modelToDto(projectRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Project does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));}

  /*  public List<int> getAllSprintForProject(Long id) {
        log.info("Fetching Sprint for Project with ID: {}", id);

        Project project = projectRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Project does not exist", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        List<int> SprintProject = projectRepository.findAll(project.getId()) ;

        return SprintProject.stream()
                .map(SprintMapper::modelToDto)
                .sorted(Comparator.comparing(SprintDTO::getId).reversed())
                .collect(Collectors.toList());
    }*/

    public ProjectDTO saveProject(ProjectDTO projectDTO)
    {
        log.info("Saving Project: {}", projectDTO);

        if ( (projectDTO.getId() != null)) {
            throw new HttpCustomException("Project ID must be null or 0 for new users.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }

        if (projectDTO.getStartDate().isAfter(projectDTO.getEndDate())) {
            throw new HttpCustomException("Start date cannot be after end date.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());

        }
        if (!checkTeamExists(projectDTO.getTeamId())) {
            throw new HttpCustomException("Team ID "+projectDTO.getTeamId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }

        projectRepository.findByNameAndDeletedFalse(projectDTO.getName())
                .ifPresent((test) -> {
                        throw new HttpCustomException("The Name " + test.getName() + " already exists.", HttpStatus.CONFLICT, HttpStatus.CONFLICT.value());
                });

        return ProjectMapper.modelToDto(projectRepository.save(ProjectMapper.dtoToModel(projectDTO)));
    }
    public boolean checkTeamExists(Long teamId) {
        try {
            teamClient.checkTeamById(teamId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Erreur lors de la vérification du team : " + e.getMessage());
            return false;
        }
    }

    public Project updateProject(Long id, ProjectDTO updatedProject)   {
        log.info("Updating Project: {} for ID: {}", updatedProject, id);
        Project existingProject = projectRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Project does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        if (!checkTeamExists(updatedProject.getTeamId())) {
            throw new HttpCustomException("Team ID "+updatedProject.getTeamId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }

            existingProject.setName(updatedProject.getName());
            existingProject.setDescription(updatedProject.getDescription());
            existingProject.setEndDate(updatedProject.getEndDate());
            existingProject.setStartDate(updatedProject.getStartDate());
            existingProject.setTeamId(updatedProject.getTeamId());
            return projectRepository.save(existingProject);
    }

    public void deleteProject(Long id)   {
        log.info("Deleting Project with ID: {}", id);
        Project Project = projectRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Project does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
            Project.setDeleted(true);
            projectRepository.save(Project);
    }


}
