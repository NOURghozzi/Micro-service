package com.userService.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    private Long id;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ]{2,12}(?: [A-Za-zÀ-ÖØ-öø-ÿ]{2,12})?$", message = "Name must contain only letters and can have one optional space. Each part must be between 2 and 12 characters long.")
    @NotNull(message = "Name cannot be null.")
    private String name;


    private boolean deleted;


}
