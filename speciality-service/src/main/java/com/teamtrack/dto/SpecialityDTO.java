package com.teamtrack.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SpecialityDTO {
    private Long id;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ]{2,12}(?: [A-Za-zÀ-ÖØ-öø-ÿ]{2,12})?$", message = "Name must contain only letters and can have one optional space. Each part must be between 2 and 12 characters long.")
    @NotNull(message = "Name cannot be null.")
    private String name;


    private boolean deleted;

}
