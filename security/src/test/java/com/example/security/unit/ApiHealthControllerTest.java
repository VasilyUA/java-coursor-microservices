package com.example.security.unit;

import com.example.security.controllers.ApiHealthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiHealthControllerTest {

    private ApiHealthController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ApiHealthController();
    }

    @Test
    void testHealthyApi() {
        ResponseEntity<String> response = controller.healthyApi();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"statusSecurityApplicationApi\":\"ok\"}", response.getBody());
    }
}
