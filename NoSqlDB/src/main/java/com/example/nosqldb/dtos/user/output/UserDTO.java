package com.example.nosqldb.dtos.user.output;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private boolean isMarried;
}
