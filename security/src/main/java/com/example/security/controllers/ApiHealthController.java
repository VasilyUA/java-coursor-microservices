package com.example.security.controllers;

import com.example.security.restclients.NoSQLServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiHealthController {

    private final NoSQLServiceClient noSQLService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> healthyApi() {
        return ResponseEntity.ok("{\"status\":\"ok\"}");
    }

    @GetMapping("/health-no-sql-endpoint")
    public ResponseEntity<String> getDataFromRemoteService() {
        return noSQLService.healthy();
    }
}
