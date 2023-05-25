package com.example.apachekafka.services;

import com.example.apachekafka.documents.User;
import com.example.apachekafka.dtos.users.UserDTO;
import com.example.apachekafka.exeptions.ResourceExistsException;
import com.example.apachekafka.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public User createUser(UserDTO userCreateDTO) {
        var user = objectMapper.convertValue(userCreateDTO, User.class);
        if (userRepo.findByEmail(userCreateDTO.getEmail()) != null) {
            throw new ResourceExistsException("User already exists with email: " + userCreateDTO.getEmail());
        }

        return userRepo.save(user);
    }

    public void sendMessageToEmail(UserDTO userDTO) {
        var user = userRepo.findByEmail(userDTO.getEmail());

        var message = user != null ? "Someone tried to sign up with your email address" : "Send list to email:" + userDTO.getEmail() + "your account was created successful";

        System.out.println("consumer2: " + message);
    }
}
