package com.example.nosqldb.dtos.user.input;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
    private int age;
    private boolean isMarried;
}
