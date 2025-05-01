package com.teamtrack.converter;

import com.teamtrack.dto.TaskDTO;
import com.teamtrack.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private static ModelMapper modelMapper  ;

    @Autowired
    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    // Convert RoleDTO to Role entity
    public static Task dtoToModel(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    // Convert Role entity to RoleDTO
    public static TaskDTO modelToDto(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }
}
