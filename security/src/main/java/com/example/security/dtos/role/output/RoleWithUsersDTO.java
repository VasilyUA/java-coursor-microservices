package com.example.security.dtos.role.output;

import com.example.security.dtos.user.output.UserDTO;
import lombok.*;
import java.util.*;

@Getter
@Setter
public class RoleWithUsersDTO {
    private Long id;
    private String name;
    private String description;
    private List<UserDTO> users;
}
