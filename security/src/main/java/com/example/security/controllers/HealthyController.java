package com.example.security.controllers;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HealthyController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> healthy() {
            return ResponseEntity.ok("{\"statusSecurityApplication\":\"ok\"}");
    }
}
