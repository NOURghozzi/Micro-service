package com.teamtrack.controller;

import com.teamtrack.dto.CommentDTO;
import com.teamtrack.entity.Comment;
import com.teamtrack.service.CommentService;
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
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @GetMapping
    @PreAuthorize("hasAuthority('LIST_COMMENT')")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        log.info("Fetching all Comments");
        return  ResponseEntity.ok(this.commentService.getAllComments());
    }

    @GetMapping("/find-comment/{id}")
    @PreAuthorize("hasAuthority('VIEW_COMMENT')")
    public ResponseEntity<CommentDTO> getComments(@PathVariable Long id) {
        log.info("Fetching Comment");
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_COMMENT')")
    public ResponseEntity<CommentDTO> saveComments(@Valid @RequestBody CommentDTO Comment) {
        log.info("Attempting to save Comment");
        return new ResponseEntity<>((commentService.saveComment(Comment)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_COMMENT')")
    public ResponseEntity<Comment> updateComments( @PathVariable Long id,@Valid @RequestBody CommentDTO updatedComment) {
        log.info("Updating Comment: {} for ID: {}", updatedComment, id);
        return ResponseEntity.ok(commentService.updateComment(id, updatedComment));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_COMMENT')")
    public void deleteComments(@PathVariable Long id) {
       log.info("Deleting Comment with ID: {}", id);
       commentService.deleteComment(id);
   }


}
