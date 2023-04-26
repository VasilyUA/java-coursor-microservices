package com.example.security.controllers;

import com.example.security.dtos.user.input.*;
import com.example.security.dtos.user.output.*;
import com.example.security.services.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Validated @RequestBody CreateUserDTO createUserDTO) {
            UserDTO userDto = userService.createUser(createUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Validated @RequestBody UpdateUserDTO updateUserDTO) throws JsonMappingException {
            UserDTO userDto = userService.updateUser(userId, updateUserDTO);
            return ResponseEntity.ok(userDto);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
            UserDTO userDto = userService.getUser(userId);
            return ResponseEntity.ok(userDto);
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"message\": \"User with id: " + userId + " deleted\"}");
    }


    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserWithRoleDTO> addUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
            UserWithRoleDTO userWithRoleDTO = userService.addUserRole(userId, roleId);
            return ResponseEntity.ok(userWithRoleDTO);
    }


    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserWithRoleDTO> removeUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
            UserWithRoleDTO userWithRoleDTO = userService.removeUserRole(userId, roleId);
            return ResponseEntity.ok(userWithRoleDTO);
    }

    @GetMapping("/{userId}/books")
    public ResponseEntity<UserWithBooksDTO> getUserWithBooks(@PathVariable Long userId) {
            UserWithBooksDTO userWithBooksDTO = userService.getUserWithBooks(userId);
            return ResponseEntity.ok(userWithBooksDTO);
    }
}
