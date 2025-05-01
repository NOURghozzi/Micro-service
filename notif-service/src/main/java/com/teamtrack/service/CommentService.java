package com.teamtrack.service;

import com.teamtrack.converter.CommentMapper;
import com.teamtrack.converter.TaskMapper;
import com.teamtrack.converter.UserMapper;
import com.teamtrack.dto.CommentDTO;
import com.teamtrack.entity.Comment;
import com.teamtrack.exception.HttpCustomException;
import com.teamtrack.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    private final JwtService jwtService;

    public CommentDTO getComment(Long id) {
        log.info("Find Comment : {}", id);
        return CommentMapper.modelToDto(commentRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Comment does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value())));
    }

    public List<CommentDTO> getAllComments() {
        log.info("Find All Comment");
        String role = jwtService.getRole();
        Long id = (jwtService.getUserId());
        // Check if the user's role is ADMIN or MANAGER
        if (role.equals("ADMIN") ||role.equals("MANAGER")) {
            List<Comment> comment = commentRepository.findByDeletedFalse();
            return comment.stream().map(CommentMapper::modelToDto).collect(Collectors.toList());
        }else {
            List<Comment> comments = commentRepository.findAllByUserIdAndDeletedFalse(id);
            return comments.stream().map(CommentMapper::modelToDto).collect(Collectors.toList());
        }
        }

    public CommentDTO saveComment(CommentDTO commentDTO) {

        log.info("Save Comment : {}", commentDTO);

        if ( (commentDTO.getId() != null)) {
                throw new HttpCustomException("Comment does not exists", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }

        return CommentMapper.modelToDto(commentRepository.save(CommentMapper.dtoToModel(commentDTO)));
    }

    public Comment updateComment(Long id, CommentDTO updatedCommentDTO) {

        log.info("Updating Comment: {} for ID: {}", updatedCommentDTO, id);

        Comment existingComment = commentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Comment does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));

        existingComment.setMessage(updatedCommentDTO.getMessage());
        existingComment.setUser(UserMapper.dtoToModel(updatedCommentDTO.getUser()));
        existingComment.setTask(TaskMapper.dtoToModel(updatedCommentDTO.getTask()));

        return commentRepository.save(existingComment);

    }

    public void deleteComment(Long id) {
        log.info("Deleted Comment : {}", id);
        Comment comment = commentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new HttpCustomException("Comment does not exists", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        comment.setDeleted(true);
        commentRepository.save(comment);
    }


}
