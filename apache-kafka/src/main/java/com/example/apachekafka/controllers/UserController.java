package com.example.apachekafka.controllers;

import com.example.apachekafka.dtos.users.User;
import com.example.apachekafka.kafka.produsers.UserProducer;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String createUser(@RequestBody User user) {
        userProducer.sendUser(user);
        return "User created";
    }
}
