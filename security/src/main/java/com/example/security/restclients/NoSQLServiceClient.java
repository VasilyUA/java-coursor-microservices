package com.example.security.restclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "remote-service", url = "http://localhost:5001")
public interface NoSQLServiceClient {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> healthy();
}
