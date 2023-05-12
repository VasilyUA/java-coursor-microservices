package com.example.nosqldb.controllers;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HealthyController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> healthy() {
        return ResponseEntity.ok("{\"statusNoSqlDbApplication\":\"ok\"}");
    }

    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> api() {
        return ResponseEntity.ok("{\"statusNoSqlDbApplicationApi\":\"ok\"}");
    }

    @GetMapping(value = "/api/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiInfo() {
        return ResponseEntity.ok("{\"statusNoSqlDbApplicationApiInfo\":\"ok\"}");
    }
}
