package com.teamtrack.service;

import com.teamtrack.Feign.ProjectTasks;
import com.teamtrack.Feign.SprintTasks;
import com.teamtrack.Feign.UserTasks;
import com.teamtrack.converter.*;
import com.teamtrack.dto.TaskDTO;
import com.teamtrack.entity.Task;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.repository.TaskRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository taskRepository;
    private ProjectTasks projectTasks ;
    private SprintTasks sprintTasks ;
    private UserTasks userTasks ;

    public TaskDTO getTask(Long id) {
        log.info("Fetching task");
        return TaskMapper.modelToDto(taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Task does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

    public List<TaskDTO> getAllTasks() {
        log.info("Fetching all tasks");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getCredentials();

        Long id = jwt.getClaim("id");
        String role = jwt.getClaim("role");
        List<Task> tasks;

        if (role.equals("ADMIN") || role.equals("MANAGER")) {
            tasks = taskRepository.findByDeletedFalse();
        }else {
            tasks = taskRepository.findByUserIdAndDeletedFalse(id);
        }
        return tasks.stream().map(TaskMapper::modelToDto).collect(Collectors.toList());
    }

   /* public List<CommentDTO> getAllCommentsTask(Long id) {
        log.info("Fetching Comments for Task with ID: {}", id);
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Task does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        List<Comment> commentsTask = commentRepository.findByTaskIdAndDeletedFalse(task.getId());

        return commentsTask.stream()
                .map(CommentMapper::modelToDto)
                .collect(Collectors.toList());

    }*/

    public TaskDTO saveTasks (TaskDTO taskDTO) {

        log.info("Saving Task: {}", taskDTO);

        if ( (taskDTO.getId() != null)) {
            throw  new HttpCustomException("Task does not exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }

        if (taskDTO.getStartDate().isAfter(taskDTO.getEndDate())) {
            throw  new HttpCustomException("Start date cannot be after end date.", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        if (!checkProjectExists(taskDTO.getProjectId())) {
            throw new HttpCustomException("Project ID "+taskDTO.getProjectId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        if (!checkSprintExists(taskDTO.getSprintId())) {
            throw new HttpCustomException("Sprint ID "+taskDTO.getSprintId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        if (!checkUserExists(taskDTO.getUserId())) {
            throw new HttpCustomException("User ID "+taskDTO.getUserId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }        //taskRepository.findByNameAndDeletedFalse(taskDTO.getName())
          //      .ifPresent((test) -> {
            //        throw new HttpCustomException("The Name " + test.getName() + " already exists.", HttpStatus.CONFLICT, HttpStatus.CONFLICT.value());
       //});*/
        return TaskMapper.modelToDto(taskRepository.save(TaskMapper.dtoToModel(taskDTO)));
    }
    public boolean checkProjectExists(Long projectId) {
        try {
            projectTasks.getProject(projectId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Erreur lors de la vérification du team : " + e.getMessage());
            return false;
        }
    }
    public boolean checkSprintExists(Long sprintId) {
        try {
            sprintTasks.getSprint(sprintId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Erreur lors de la vérification du team : " + e.getMessage());
            return false;
        }
    }
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getTasksBySprintId(Long sprintId) {
        return taskRepository.findBySprintId(sprintId);
    }
    public boolean checkUserExists(Long userId) {
        try {
            userTasks.getUser(userId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Erreur lors de la vérification du team : " + e.getMessage());
            return false;
        }
    }
    public Task updateTasks(Long id, TaskDTO updatedTaskDTO) {
        log.info("Updating Task: {} for ID: {}", updatedTaskDTO, id);
        if (!checkProjectExists(updatedTaskDTO.getProjectId())) {
            throw new HttpCustomException("Project ID "+updatedTaskDTO.getProjectId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        if (!checkSprintExists(updatedTaskDTO.getSprintId())) {
            throw new HttpCustomException("Sprint ID "+updatedTaskDTO.getSprintId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        if (!checkUserExists(updatedTaskDTO.getUserId())) {
            throw new HttpCustomException("User ID "+updatedTaskDTO.getUserId()+" not found.", HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
        }
        Task existingTask = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Task does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        existingTask.setName(updatedTaskDTO.getName());
        existingTask.setDescription(updatedTaskDTO.getDescription());
        existingTask.setEndDate(updatedTaskDTO.getEndDate());
        existingTask.setStartDate(updatedTaskDTO.getStartDate());
        existingTask.setUserId(updatedTaskDTO.getUserId());
        existingTask.setSprintId(updatedTaskDTO.getSprintId());

        return taskRepository.save(existingTask);

    }

    public Task updateStateTasks(Long id, TaskDTO taskDTOstate) {
        log.info("Updating state Task: {} for ID: {}", taskDTOstate, id);
        log.info("state{}", taskDTOstate.getState());
        Task existingTask = taskRepository.findByIdAndDeletedFalse(id)
                        .orElseThrow(() -> new HttpCustomException("Task does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        existingTask.setState(taskDTOstate.getState());
         return taskRepository.save(existingTask);

    }

    public void deleteTask(Long id) {
        log.info("Deleting Task with ID: {}", id);
        Task task = taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Task does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
            task.setDeleted(true);
            taskRepository.save(task);

    }


}
