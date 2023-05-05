package com.example.awscursor.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "user")
public class User {
    @DynamoDBHashKey(attributeName = "id")
    private Long id;

    @DynamoDBAttribute(attributeName = "name")
    private String name;
}
