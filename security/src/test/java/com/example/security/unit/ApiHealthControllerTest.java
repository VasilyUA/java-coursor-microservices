package com.example.security.unit;

import com.example.security.controllers.ApiHealthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiHealthControllerTest {

    private ApiHealthController apiHealthController;

    @BeforeEach
    public void setUp() {
        apiHealthController = new ApiHealthController();
    }

    @Test
    public void testHealthyApi() {
        ResponseEntity<String> response = apiHealthController.healthyApi();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"status\":\"ok\"}", response.getBody());
    }
}
