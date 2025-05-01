package com.teamtrack.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SprintDTO {

    private Long id;

    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9]{2,32}(?:\\s[A-Za-zÀ-ÖØ-öø-ÿ0-9]{1,32})*$",
            message = "Name must contain only letters, digits, and can have multiple spaces. The first part must be between 2 and 32 characters long, and each subsequent part must be between 1 and 32 characters long."
    )
    @NotNull(message = "Name cannot be null.")
    private String name;


    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ0-9.,!?'\"()\\s-]{1,255}$", message = "Description can only contain letters, numbers, punctuation, and spaces, and must be between 1 and 255 characters long.")
    @NotNull(message = "Description cannot be Null")
    private String description;

    private String state ;


    @NotNull(message = "The event date and time must not be null")
    private LocalDateTime startDate;

    @NotNull(message = "The event date and time must not be null")
    private LocalDateTime endDate;

    @NotNull(message = "project cannot be Empty")
    private Long projectId;

    private boolean deleted;

}
