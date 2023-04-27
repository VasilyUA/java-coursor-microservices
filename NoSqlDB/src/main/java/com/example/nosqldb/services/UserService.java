package com.example.nosqldb.services;

import com.example.nosqldb.documents.User;
import com.example.nosqldb.dtos.user.input.UserCreateDTO;
import com.example.nosqldb.dtos.user.input.UserUpdateDTO;
import com.example.nosqldb.dtos.user.output.UserDTO;
import com.example.nosqldb.exceptions.NotFoundException;
import com.example.nosqldb.exceptions.ResourceExistsException;
import com.example.nosqldb.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User user = objectMapper.convertValue(userCreateDTO, User.class);
        if (userRepo.findByEmail(userCreateDTO.getEmail()) != null) {
            throw new ResourceExistsException("User already exists with email: " + userCreateDTO.getEmail());
        }
        return convertToDto(userRepo.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found with email: " + email);
        }
        return convertToDto(user);
    }

    public List<UserDTO> getUsersByFirstName(String firstName) {
        return userRepo.findByFirstName(firstName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByLastName(String lastName) {
        return userRepo.findByLastName(lastName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersOlderThan(int age) {
        return userRepo.findByAgeGreaterThan(age).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getMarriedUsers(boolean isMarried) {
        return userRepo.findByMarried(isMarried).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(ObjectId id, UserUpdateDTO userUpdateDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setAge(userUpdateDTO.getAge());
        user.setMarried(userUpdateDTO.isMarried());
        return convertToDto(userRepo.save(user));
    }

    public void deleteUser(ObjectId id) {
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }

    private UserDTO convertToDto(User user) {
        return objectMapper.convertValue(user, UserDTO.class);
    }
}
