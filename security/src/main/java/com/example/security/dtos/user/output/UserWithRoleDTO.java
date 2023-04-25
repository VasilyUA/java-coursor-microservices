package com.example.security.dtos.user.output;

import com.example.security.dtos.role.output.RoleDTO;
import lombok.*;

import java.util.*;

@Getter
@Setter
public class UserWithRoleDTO {
    private Long id;
    private String email;
    private List<RoleDTO> roles;
}
