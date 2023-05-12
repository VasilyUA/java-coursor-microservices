package com.example.security.restclients.dtos.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private boolean isMarried;
}
