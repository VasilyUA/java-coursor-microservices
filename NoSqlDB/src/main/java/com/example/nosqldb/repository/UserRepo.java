package com.example.nosqldb.repository;

import com.example.nosqldb.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String>  {
    User findByEmail(String email);
    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    List<User> findByAgeGreaterThan(int age);
    List<User> findByMarried(Boolean isMarried);
}