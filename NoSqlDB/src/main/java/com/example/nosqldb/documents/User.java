package com.example.nosqldb.documents;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private boolean married;
}
