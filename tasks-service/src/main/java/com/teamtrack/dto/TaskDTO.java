package com.teamtrack.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskDTO {
    private Long id;

    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9]{2,12}(?: [A-Za-zÀ-ÖØ-öø-ÿ0-9]{1,12})?$",
            message = "Name must contain only letters, digits, and can have one optional space. The first part must be between 2 and 12 characters long, and the second part, if present, must be between 1 and 12 characters long."
    )
    @NotNull(message = "Name cannot be null.")
    private String name;

    private String state ;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ0-9.,!?'\"()\\s-]{1,255}$", message = "Description can only contain letters, numbers, punctuation, and spaces, and must be between 1 and 255 characters long.")
    @NotNull(message = "Description cannot be Null")
    private String description;

    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9]{2,32}(?:\\s[A-Za-zÀ-ÖØ-öø-ÿ0-9]{1,32})*$",
            message = "Description can only contain letters, numbers, punctuation, and spaces, and must be between 1 and 255 characters long.")
    @NotNull(message = "Description cannot be Null")
    private String priority;

    @NotNull(message = "cannot be Null")
    private Long projectId;

    @NotNull(message = "cannot be Null")
    private Long userId;

    private Long sprintId ;

    @NotNull(message = "The event date and time must not be null")
    private LocalDateTime startDate;

    @NotNull(message = "The event date and time must not be null")
    private LocalDateTime endDate;

    private boolean deleted;

   // private TaskHistoryDTO historical;


}
