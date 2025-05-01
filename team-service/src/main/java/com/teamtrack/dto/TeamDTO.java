package com.teamtrack.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TeamDTO {
    private Long id;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ]{2,12}(?: [A-Za-zÀ-ÖØ-öø-ÿ]{2,12})?$", message = "Name must contain only letters and can have one optional space. Each part must be between 2 and 12 characters long.")
    @NotNull(message = "Name cannot be null.")
    private String name;




    @Size(min = 1, message = "The list must contain at least one user")
    private List<Long> userIds;

    private boolean deleted;

}



