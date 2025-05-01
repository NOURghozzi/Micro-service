package com.teamtrack.service;

import com.teamtrack.Feign.ProjectSprints;
import com.teamtrack.converter.SprintMapper;
import com.teamtrack.dto.SprintDTO;
import com.teamtrack.entity.Sprint;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.repository.SprintRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class SprintService {
    private SprintRepository sprintRepository;
private  ProjectSprints projectSprints;
    public SprintDTO getSprint(Long id) {
        log.info("Fetching Sprint");
        return SprintMapper.modelToDto(sprintRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Sprint does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

   public List<SprintDTO> getAllSprints(Long id) {
       log.info("Fetching all Sprints for project id :"+id);
       List<Sprint> sprints = sprintRepository.findByProjectIdAndDeletedFalse(id);
       return sprints.stream().map(SprintMapper::modelToDto).collect(Collectors.toList());
   }
    public List<SprintDTO> getAllSprintsByProject(Long projectId) {
        if (!checkProjectExists(projectId)) {
            throw new HttpCustomException("Project ID "+projectId+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        List<Sprint> sprints = sprintRepository.findByProjectId(projectId);

        return sprints.stream().map(SprintMapper::modelToDto).collect(Collectors.toList());

    }
  /*  public List<TaskDTO> getAllTaskForSprints(long id) {
        log.info("Fetching Tasks for Sprint with ID: {}", id);
        Sprint sprint = sprintRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Sprint does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        List<Task> taskSprint = taskRepository.findBySprintIdAndDeletedFalse(sprint.getId());

        return taskSprint.stream()
                .map(TaskMapper::modelToDto)
                .sorted(Comparator.comparing(TaskDTO::getId).reversed())
                .collect(Collectors.toList());
    }
*/
    public SprintDTO saveSprint(SprintDTO sprintDTO) {

        log.info("Saving Sprint: {}", sprintDTO);

        if ( (sprintDTO.getId() != null)) {
            throw new HttpCustomException("Sprint ID must be null or 0 .", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }

        if (sprintDTO.getStartDate().isAfter(sprintDTO.getEndDate())) {
            throw new HttpCustomException("Start date cannot be after end date.", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        if (!checkProjectExists(sprintDTO.getProjectId())) {
            throw new HttpCustomException("Project ID "+sprintDTO.getProjectId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        return SprintMapper.modelToDto(sprintRepository.save(SprintMapper.dtoToModel(sprintDTO)));
    }
    public boolean checkProjectExists(Long projectId) {
        try {
            projectSprints.getProject(projectId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Erreur lors de la vérification du team : " + e.getMessage());
            return false;
        }
    }

    public Sprint updateSprint(Long id, SprintDTO updatedSprintDTO) {
        log.info("Updating Sprint: {} for ID: {}", updatedSprintDTO, id);
        if (!checkProjectExists(updatedSprintDTO.getProjectId())) {
            throw new HttpCustomException("Project ID "+updatedSprintDTO.getProjectId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        Sprint existingSprint = sprintRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Sprint does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));

            existingSprint.setName(updatedSprintDTO.getName());
            existingSprint.setDescription(updatedSprintDTO.getDescription());
            existingSprint.setEndDate(updatedSprintDTO.getEndDate());
            existingSprint.setStartDate(updatedSprintDTO.getStartDate());
            existingSprint.setProjectId(updatedSprintDTO.getProjectId());
        return sprintRepository.save(existingSprint);

    }

    public Sprint updateStateSprints(Long id, SprintDTO sprintDTO) {
        log.info("Updating state Task: {} for ID: {}", sprintDTO, id);
        log.info("state"+sprintDTO.getState());
        if (!checkProjectExists(sprintDTO.getProjectId())) {
            throw new HttpCustomException("Project ID "+sprintDTO.getProjectId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        Sprint existingTask = sprintRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Task does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        existingTask.setState(sprintDTO.getState());
        return sprintRepository.save(existingTask);

    }
    public void deleteSprint(Long id) {
        log.info("Deleting Sprint with ID: {}", id);
        Sprint sprint = sprintRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Sprint does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
            sprint.setDeleted(true);
            sprintRepository.save(sprint);
    }
    }
