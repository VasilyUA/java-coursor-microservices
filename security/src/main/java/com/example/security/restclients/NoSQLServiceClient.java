package com.example.security.restclients;

import com.example.security.restclients.dtos.user.RequestUserCreateDTO;
import com.example.security.restclients.dtos.user.ResponseUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "remote-service", url = "${app.no-sql-service.url}")
public interface NoSQLServiceClient {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> healthy();
    @GetMapping(value = "/api/info", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> apiInfo();

    @GetMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ResponseUserDTO> getAllUsers();

    @PostMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseUserDTO createUser(@RequestBody RequestUserCreateDTO requestUserCreateDTO);
}
