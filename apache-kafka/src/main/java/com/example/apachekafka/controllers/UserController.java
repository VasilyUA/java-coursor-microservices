package com.example.apachekafka.controllers;

import com.example.apachekafka.dtos.users.UserDTO;
import com.example.apachekafka.kafka.produsers.UserProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserProducer userProducer;

    @PostMapping
    public String createUser(@Validated @RequestBody UserDTO userDTO) {
        userProducer.sendUser(userDTO);
        return "User produced";
    }
}
