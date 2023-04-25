package com.example.security.services;

import com.example.security.dtos.role.input.*;
import com.example.security.dtos.role.output.*;
import com.example.security.dtos.user.output.UserDTO;
import com.example.security.entitys.*;
import com.example.security.exceptions.*;
import com.example.security.repositories.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;

    public RoleDTO createRole(CreateRoleDTO createRoleDTO) {
        Role existingRole = roleRepo.findByName(createRoleDTO.getName());
        if (existingRole != null) {
            throw new NotFoundException("Role already exists with name: " + createRoleDTO.getName());
        }

        Role role = objectMapper.convertValue(createRoleDTO, Role.class);
        Role savedRole = roleRepo.save(role);
        return convertToDto(savedRole);
    }

    public RoleDTO updateRole(Long roleId, UpdateRoleDTO updateRoleDTO) throws JsonMappingException {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));
        objectMapper.updateValue(role, updateRoleDTO);
        Role updatedRole = roleRepo.save(role);
        return convertToDto(updatedRole);
    }

    public RoleDTO getRole(Long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));
        return convertToDto(role);
    }

    public List<RoleDTO> getAllRoles() {
        var roles = roleRepo.findAll();
        return roles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteRole(Long roleId) {
        if (!roleRepo.existsById(roleId)) {
            throw new NotFoundException("Role not found with id: " + roleId);
        }
        roleRepo.deleteById(roleId);
    }

    public RoleWithUsersDTO addRoleToUser(Long roleId, Long userId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        role.addUser(user);
        roleRepo.save(role);
        return convertToDtoWithUsers(role);
    }

    public RoleWithUsersDTO removeRoleForUser(Long roleId, Long userId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + roleId));

        List<User> usersToRemove = role.getUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .peek(u -> u.getRoles().remove(role))
                .toList();

        if (usersToRemove.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId + " for role with id: " + roleId);
        }

        role.getUsers().removeAll(usersToRemove);
        roleRepo.save(role);
        return convertToDtoWithUsers(role);
    }


    private RoleDTO convertToDto(Role role) {
        return objectMapper.convertValue(role, RoleDTO.class);
    }

    private RoleWithUsersDTO convertToDtoWithUsers(Role role) {
        List<User> users = role.getUsers();
        if (users == null) {
            return objectMapper.convertValue(role, RoleWithUsersDTO.class);
        }

        List<UserDTO> userDtoList = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        role.setUsers(null);

        RoleWithUsersDTO roleWithUsersDTO = objectMapper.convertValue(role, RoleWithUsersDTO.class);
        roleWithUsersDTO.setUsers(userDtoList);

        return roleWithUsersDTO;
    }

    private UserDTO convertToDto(User user) {
        return objectMapper.convertValue(user, UserDTO.class);
    }
}
