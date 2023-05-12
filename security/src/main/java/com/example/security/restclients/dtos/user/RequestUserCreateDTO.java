package com.example.security.restclients.dtos.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class RequestUserCreateDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @NotNull
    private int age;
    @NotNull
    private boolean isMarried;
}
