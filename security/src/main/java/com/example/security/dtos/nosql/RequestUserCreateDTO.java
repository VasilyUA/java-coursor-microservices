package com.example.security.dtos.nosql;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
