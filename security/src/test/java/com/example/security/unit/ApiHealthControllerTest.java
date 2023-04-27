package com.example.security.unit;

import com.example.security.controllers.ApiHealthController;
import com.example.security.restclients.NoSQLServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ApiHealthControllerTest {

    private ApiHealthController controller;

    @Mock
    private NoSQLServiceClient noSQLService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ApiHealthController(noSQLService);
    }

    @Test
    void testHealthyApi() {
        ResponseEntity<String> response = controller.healthyApi();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"status\":\"ok\"}", response.getBody());
    }

    @Test
    void testGetDataFromRemoteService() {
        when(noSQLService.healthy()).thenReturn(new ResponseEntity<>("{\"status\":\"ok\"}", HttpStatus.OK));
        ResponseEntity<String> response = controller.getDataFromRemoteService();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"status\":\"ok\"}", response.getBody());
    }
}
