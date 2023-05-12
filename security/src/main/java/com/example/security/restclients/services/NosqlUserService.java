package com.example.security.restclients.services;

import com.example.security.restclients.NoSQLServiceClient;
import com.example.security.restclients.dtos.user.RequestUserCreateDTO;
import com.example.security.restclients.dtos.user.ResponseUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NosqlUserService {

    private final NoSQLServiceClient noSQLServiceClient;

    public ResponseEntity<String> healthy() {
        return noSQLServiceClient.healthy();
    }

    public ResponseEntity<String> apiInfo() {
        return noSQLServiceClient.apiInfo();
    }

    public List<ResponseUserDTO> getAllUsers() {
        System.out.println("NosqlUserService.getAllUsers(2)");
        return noSQLServiceClient.getAllUsers();
    }

    public ResponseUserDTO createUser(RequestUserCreateDTO requestUserCreateDTO) {
        return noSQLServiceClient.createUser(requestUserCreateDTO);
    }

}
