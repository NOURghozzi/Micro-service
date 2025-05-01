package com.teamtrack.converter;

import com.teamtrack.dto.CommentDTO;
import com.teamtrack.entity.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private static  ModelMapper modelMapper;

    @Autowired
    public CommentMapper(ModelMapper modelMapper) {
        CommentMapper.modelMapper = modelMapper;
    }
    // Convert RoleDTO to Role entity
    public static Comment dtoToModel(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    // Convert Role entity to RoleDTO
    public static CommentDTO modelToDto(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }
}
