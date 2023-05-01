package com.example.nosqldb.controllers;

import com.example.nosqldb.dtos.user.input.UserCreateDTO;
import com.example.nosqldb.dtos.user.input.UserUpdateDTO;
import com.example.nosqldb.dtos.user.output.UserDTO;
import com.example.nosqldb.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Validated @RequestBody UserCreateDTO user) {
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<List<UserDTO>> getUsersByFirstName(@PathVariable String firstName) {
        List<UserDTO> users = userService.getUsersByFirstName(firstName);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<UserDTO>> getUsersByLastName(@PathVariable String lastName) {
        List<UserDTO> users = userService.getUsersByLastName(lastName);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/olderthan/{age}")
    public ResponseEntity<List<UserDTO>> getUsersOlderThan(@PathVariable int age) {
        List<UserDTO> users = userService.getUsersOlderThan(age);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/married/{isMarried}")
    public ResponseEntity<List<UserDTO>> getMarriedUsers(@PathVariable boolean isMarried) {
        List<UserDTO> users = userService.getMarriedUsers(isMarried);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable ObjectId id, @Validated @RequestBody UserUpdateDTO updatedUser) {
        var user = userService.updateUser(id, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable ObjectId id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }
}
