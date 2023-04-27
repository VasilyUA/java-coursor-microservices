package com.example.security.services;


import com.example.security.configs.secyrity.JwtTokenService;
import com.example.security.dtos.auth.input.AuthenticationRequestDTO;
import com.example.security.dtos.auth.output.AuthenticationResponseDTO;
import com.example.security.dtos.auth.input.RegistrationRequestDTO;
import com.example.security.entitys.Role;
import com.example.security.entitys.User;
import com.example.security.exceptions.ResourceExistsException;
import com.example.security.repositories.RoleRepo;
import com.example.security.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo repository;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public AuthenticationResponseDTO register(RegistrationRequestDTO request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceExistsException("User already exists with email: " + request.getEmail());
        }

        var user = objectMapper.convertValue(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var existingRole = roleRepo.findByName("ROLE_USER");
        if (existingRole == null) {
            var createUserRole = new Role();
            createUserRole.setName("ROLE_USER");
            createUserRole.setDescription("User role");
            createUserRole.setUsers(new ArrayList<>());
            existingRole = roleRepo.save(createUserRole);
        }

        user.enrollInCourse(existingRole);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .user(userService.convertToDtoWithRoles(user))
                .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .user(userService.convertToDtoWithRoles(user))
                .build();
    }
}