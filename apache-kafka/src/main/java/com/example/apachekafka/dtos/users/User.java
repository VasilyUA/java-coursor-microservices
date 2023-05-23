package com.example.apachekafka.dtos.users;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private String email;
}
