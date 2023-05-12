package com.example.security.configs.secyrity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        Logger logger = LoggerFactory.getLogger(getClass());
        ObjectMapper objectMapper = new ObjectMapper();
        return (request, response, authException) -> {
            String message = authException != null && authException.getMessage() != null ? authException.getMessage() : "Unauthorized";
            logger.info("Sending error message: {}", message);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", message);
            errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);

            try {
                response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
            } catch (IOException e) {
                logger.error("Failed to write JSON error response: {}", e.getMessage());
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/", "/api/auth/**", "/docs/**", "/swagger-ui/**", "/api", "/api/user-nosql-service/**")
                .permitAll()
                .requestMatchers("/api/roles/**", "/api/user/{userId}/books").hasRole("USER")
                .requestMatchers("/api/roles/**", "/api/user/**").hasRole("ADMIN")
                .requestMatchers( "/api/roles/**", "/api/user/**", "/api/book/**", "/api/user/{userId}/books")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}