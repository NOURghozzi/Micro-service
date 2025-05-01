package com.userService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionDTO {

    private Long id;

    @NotNull(message = "Etat cannot be Null")
    @Pattern(regexp = "^[\\p{L}0-9.,!?'\"()\\s-_]{1,255}$", message = "Etat can only contain letters, numbers, punctuation, underscores, and spaces, and must be between 1 and 255 characters long.")
    private String etat;

    @Size(min = 1, message = "The list must contain at least one user")
    @NotNull(message = "Roles cannot be null")
    private List<RoleDTO> roles;

    private boolean deleted;

}
