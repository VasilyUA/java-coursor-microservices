package com.example.security.controllers;

import com.example.security.dtos.role.input.CreateRoleDTO;
import com.example.security.dtos.role.input.UpdateRoleDTO;
import com.example.security.dtos.role.output.RoleDTO;
import com.example.security.dtos.role.output.RoleWithUsersDTO;
import com.example.security.services.RoleService;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody CreateRoleDTO createRoleDTO) {
            RoleDTO roleDTO = roleService.createRole(createRoleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(roleDTO);
    }


    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long roleId, @RequestBody UpdateRoleDTO updateRoleDTO) throws JsonMappingException {
            RoleDTO roleDTO = roleService.updateRole(roleId, updateRoleDTO);
            return ResponseEntity.ok(roleDTO);
    }


    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long roleId) {
            RoleDTO roleDTO = roleService.getRole(roleId);
            return ResponseEntity.ok(roleDTO);
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
            List<RoleDTO> roles = roleService.getAllRoles();
            return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
            roleService.deleteRole(roleId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"message\": \"Role with id: " + roleId + " was deleted\"}");
    }


    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<RoleWithUsersDTO> addRoleToUser(@PathVariable Long roleId, @PathVariable Long userId) {
            RoleWithUsersDTO roleWithUsersDTO = roleService.addRoleToUser(roleId, userId);
            return ResponseEntity.ok(roleWithUsersDTO);
    }


    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<RoleWithUsersDTO> removeRoleForUser(@PathVariable Long roleId, @PathVariable Long userId) {
            RoleWithUsersDTO roleWithUsersDTO = roleService.removeRoleForUser(roleId, userId);
            return ResponseEntity.ok(roleWithUsersDTO);
    }

}
