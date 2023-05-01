package com.example.nosqldb.dtos.user.input;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
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
