package com.example.security.dtos.user.input;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
public class UpdateUserDTO {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
}
