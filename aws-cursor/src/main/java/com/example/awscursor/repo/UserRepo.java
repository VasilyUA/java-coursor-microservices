package com.example.awscursor.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.awscursor.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepo {

    private final DynamoDBMapper dynamoDBMapper;

    public void save(User user) {
        dynamoDBMapper.save(user);
    }

    public User findById(Long id) {
        return dynamoDBMapper.load(User.class, id);
    }
}
