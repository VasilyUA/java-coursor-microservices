package com.example.security.dtos.auth.output;

import com.example.security.dtos.user.output.UserWithRoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("user")
    private UserWithRoleDTO user;
}