package com.teamtrack.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import jakarta.validation.constraints.NotNull;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProjectDTO {
    private Long id;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ]{2,12}(?: [A-Za-zÀ-ÖØ-öø-ÿ]{2,12})?$", message = "Name must contain only letters and can have one optional space. Each part must be between 2 and 12 characters long.")
    @NotNull(message = "Name cannot be null.")
    private String name;


    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ0-9.,!?'\"()\\s-]{1,255}$", message = "Description can only contain letters, numbers, punctuation, and spaces, and must be between 1 and 255 characters long.")
    @NotNull(message = "Description cannot be Null")
    private String description;

    @NotNull(message = "The event date and time must not be null")
    private LocalDateTime startDate;

    @NotNull(message = "The event date and time must not be null")
    private LocalDateTime endDate;

    @NotNull(message = "team cannot be Null")
    private Long teamId;

    private boolean deleted;

}
