package com.example.security.controllers;

import com.example.security.restclients.dtos.user.RequestUserCreateDTO;
import com.example.security.restclients.dtos.user.ResponseUserDTO;
import com.example.security.restclients.services.NosqlUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-nosql-service")
@RequiredArgsConstructor
public class NoSqlController {
    private final NosqlUserService userService;

    @GetMapping
    public ResponseEntity<String> healthy() {
        return userService.healthy();
    }

    @GetMapping("/info")
    public ResponseEntity<String> apiInfo() {
        return userService.apiInfo();
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        System.out.println("NoSqlController.getAllUsers(1)");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUserDTO> createUser(@Validated @RequestBody RequestUserCreateDTO requestUserCreateDTO) {
        var user = userService.createUser(requestUserCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
