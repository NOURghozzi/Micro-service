package com.teamtrack.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDTO {

    private Long id;

    @NotNull(message = "User cannot be Null")
    private UserDTO user ;

    private TaskDTO task ;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ0-9.,!?'\"()\\s-]{1,255}$", message = "Description can only contain letters, numbers, punctuation, and spaces, and must be between 1 and 255 characters long.")
    @NotNull(message = "Message cannot be Null")
    private String message;

    private boolean deleted;

}
