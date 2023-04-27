package com.example.nosqldb.unit;

import com.example.nosqldb.controllers.HealthyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthyControllerTest {

    private HealthyController healthyController;

    @BeforeEach
    void setUp() {
        healthyController = new HealthyController();
    }

    @Test
    void healthyTest() {
        ResponseEntity<String> response = healthyController.healthy();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"status\":\"ok\"}", response.getBody());
    }

    @Test
    void apiTest() {
        ResponseEntity<String> response = healthyController.api();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"status\":\"ok\"}", response.getBody());
    }
}
