package com.userService.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    private Long id;

    @Pattern(regexp ="^[A-Za-zÀ-ÖØ-öø-ÿ]{2,12}(?: [A-Za-zÀ-ÖØ-öø-ÿ]{2,12})?$", message = "Name must contain only letters and can have one optional space. Each part must be between 2 and 12 characters long.")
    @NotNull(message = "Name cannot be null.")
    private String name;


    private String preName;

    @NotNull(message = "email cannot be Null")
    @Email
    private String email;

    @NotBlank(message = "Invalid Phone number: Empty number")
    @Pattern(regexp = "^\\d{8}$", message = "Invalid phone number")
    private String phone;

    @Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long, and include one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=!). No whitespace allowed.")
    @NotNull(message = "Password cannot be null.")
    private String password;


    private RoleDTO role;

    private boolean deleted;

    private List<Long> speciality;

    private String state ;

    private LocalDateTime createdAt;

}
