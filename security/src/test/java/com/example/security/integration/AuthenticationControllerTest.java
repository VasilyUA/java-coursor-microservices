package com.example.security.integration;

import com.example.security.dtos.auth.input.AuthenticationRequestDTO;
import com.example.security.dtos.auth.input.RegistrationRequestDTO;
import com.example.security.dtos.auth.output.AuthenticationResponseDTO;
import com.example.security.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.25"))
            .withDatabaseName("test")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    @Test
    public void testRegistration() throws Exception {
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setEmail("register@example.com");
        registrationRequestDTO.setPassword("Register1234");

        var response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isCreated());

        AuthenticationResponseDTO authResponse = objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), AuthenticationResponseDTO.class);

        assertNotNull(authResponse.getAccessToken());
        assertNotNull(authResponse.getUser());
        assertEquals(authResponse.getUser().getEmail(), registrationRequestDTO.getEmail());
    }

    @Test
    public void testAuthentication() throws Exception {
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setEmail("authenticate@example.com");
        registrationRequestDTO.setPassword("Authenticate1234");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isCreated());

        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail("authenticate@example.com");
        authenticationRequestDTO.setPassword("Authenticate1234");

        String authResponseJson = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AuthenticationResponseDTO authResponse = objectMapper.readValue(authResponseJson, AuthenticationResponseDTO.class);
        assertNotNull(authResponse.getAccessToken());
        assertNotNull(authResponse.getUser());
        assertEquals("authenticate@example.com", authResponse.getUser().getEmail());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}
