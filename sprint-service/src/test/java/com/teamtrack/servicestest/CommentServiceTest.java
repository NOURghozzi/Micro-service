package com.teamtrack.servicestest;

import com.teamtrack.TeamtrackApplication;
import com.teamtrack.dto.*;
import com.teamtrack.entity.*;
import com.teamtrack.repository.CommentRepository;
import com.teamtrack.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamtrackApplication.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    public CommentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldSaveCommentSuccessfully() {
        UserDTO userDTO = new UserDTO();
        TaskDTO taskDTO = new TaskDTO();
        CommentDTO commentDTO = CommentDTO.builder()
                .user(userDTO)
                .message("test")  // Ensure the field name matches the one in your DTO
                .task(taskDTO)
                .build();

        // Prepare entity corresponding to DTO
        User user = new User();
        Task task = new Task();
        Comment comment = Comment.builder()
                .user(user)
                .message("test")  // Ensure this matches the TasksDTO field
                .task(task)
                .deleted(false)
                .build();

        // Mock repository behavior
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Call the service method
        CommentDTO savedComment = commentService.saveComment(commentDTO);

        // Verify interactions and assertions
        assertNotNull(savedComment, "Saved Comment should not be null");
        assertEquals(commentDTO.getMessage(), savedComment.getMessage(), "Comment name should match");
        assertEquals(commentDTO.getUser(), savedComment.getUser(), "Comment user should match");
        assertEquals(commentDTO.getTask(), savedComment.getTask(), "Comment sprint should match");

     }

    @Test
    public void shouldWhenFindByIdAndDeletedFalseThenReturnComments() {
        UserDTO userDTO = new UserDTO();
        TaskDTO taskDTO = new TaskDTO();
        CommentDTO commentDTO = CommentDTO.builder()
                .id(1L)
                .user(userDTO)
                .message("test")  // Ensure the field name matches the one in your DTO
                .task(taskDTO)
                .build();

        // Prepare entity corresponding to DTO
        User user = new User();
        Task task = new Task();
        Comment comment = Comment.builder()
                .id(2L)
                .user(user)
                .message("test")  // Ensure this matches the TasksDTO field
                .task(task)
                .deleted(false)
                .build();
        when(commentRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.ofNullable(comment));

        // Call the method under test
        CommentDTO found = commentService.getComment(1L);

        // Verify interactions and assertions
        assertNotNull(found, "Found comment should not be null");
      //  assertThat(found).isPresent();  // Check that the Optional contains a value
        assertThat(found.getMessage()).isEqualTo(commentDTO.getMessage());
        assertThat(found.isDeleted()).isFalse();

        // Verify that the repository method was called with the correct argument
        verify(commentRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    public void shouldWhenGetAllComments_thenReturnCommentsList() {
        User user = new User();
        Task task = new Task();
        Comment comment = Comment.builder()
                .id(1L)
                .user(user)
                .message("test")  // Ensure this matches the TasksDTO field
                .task(task)
                .deleted(false)
                .build();
        Comment comment1 = Comment.builder()
                .id(2L)
                .user(user)
                .message("test1")  // Ensure this matches the TasksDTO field
                .task(task)
                .deleted(false)
                .build();

        // Mock the repository method to return a list of tasks
        when(commentRepository.findByDeletedFalse()).thenReturn(Arrays.asList(comment, comment1));

        // Call the method under test
        List<CommentDTO> commentDTOS = commentService.getAllComments();

        // Verify interactions and assertions
        assertThat(commentDTOS).hasSize(2);
        assertThat(commentDTOS).extracting(CommentDTO::getMessage).containsExactlyInAnyOrder("test", "test1");

        // Verify that the repository method was called once
        verify(commentRepository, times(1)).findByDeletedFalse();
    }
    @Test
    public void whenDeleteCommentsThenCommentsIsDeleted() {
        Long commentId = 4L;
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setDeleted(false);

        // when
        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(Optional.of(comment));
        doAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setDeleted(true);
            return null;
        }).when(commentRepository).save(any(Comment.class));

        // Call the service method that should invoke deleteById
        commentService.deleteComment(commentId);

        // then
        assertThat(comment.isDeleted()).isTrue();  // Verify that the team is marked as deleted
        verify(commentRepository).save(comment);  // Verify that the save method was called
    }

    @Test
    public void testUpdateCommentSuccess() {
        Long commentId = 1L;
       
        User user = new User();
        Role role =new Role();
        user.setRole(role);
        Task task = new Task();
        Comment comment = Comment.builder()
                .id(commentId)
                .user(user)
                .message("test")  // Ensure this matches the TasksDTO field
                .task(task)
                .deleted(false)
                .build();
        UserDTO userDTO = new UserDTO();
        TaskDTO taskDTO = new TaskDTO();
        RoleDTO roleDTO =new RoleDTO();
        userDTO.setRole(roleDTO);
        CommentDTO updatedCommentDTO = new CommentDTO();
        updatedCommentDTO.setMessage("New Name");
        updatedCommentDTO.setUser(userDTO);
        updatedCommentDTO.setTask(taskDTO);

        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment updatedComment = commentService.updateComment(comment.getId(), updatedCommentDTO);
        assertEquals(updatedCommentDTO.getMessage(), updatedComment.getMessage());
          }
    @Test
    public void testUpdateCommentsNotFound() {
        Long commentId = 1L;
        CommentDTO updatedCommentDTO = new CommentDTO();

        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.updateComment(commentId, updatedCommentDTO);
        });

        assertEquals("Comment with id not found", exception.getMessage());
    }
    @Test
    public void testSaveComments_InvalidId_ThrowsException() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.saveComment(commentDTO);
        });
        assertEquals("Comment ID must be null or 0 for new users.", exception.getMessage());

    }

}
