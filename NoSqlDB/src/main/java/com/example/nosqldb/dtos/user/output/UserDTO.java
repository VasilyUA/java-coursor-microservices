package com.example.nosqldb.dtos.user.output;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private boolean isMarried;
}
