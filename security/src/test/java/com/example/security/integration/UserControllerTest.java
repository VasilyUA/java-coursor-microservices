package com.example.security.integration;


import com.example.security.dtos.auth.input.*;
import com.example.security.dtos.auth.output.AuthenticationResponseDTO;
import com.example.security.dtos.user.input.*;
import com.example.security.dtos.user.output.UserDTO;
import com.example.security.dtos.user.output.UserWithRoleDTO;
import com.example.security.entitys.*;
import com.example.security.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {


    public static final String EMAIL = "admin@localhost.com";
    public static final String ROLE = "ROLE_ADMIN";
    public static final String PASSWORD = "Book1234";

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
        createUserWithRoleAdmin();
    }

    private void createUserWithRoleAdmin() {
        var role = new Role();
        role.setName(ROLE);
        role.setDescription("Admin role");
        var adminRole = roleRepository.save(role);

        var user = new User();
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        var userAdmin = userRepository.save(user);

        userAdmin.enrollInCourse(adminRole);
        userRepository.save(userAdmin);
    }

    private AuthenticationResponseDTO obtainAccess() throws Exception {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail(EMAIL);
        authenticationRequestDTO.setPassword(PASSWORD);

        String authResponseJson = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(authResponseJson, AuthenticationResponseDTO.class);
    }

    @Test
    public void testUserWithRoleAdminInDb() {
        var user = userRepository.findByEmail(EMAIL).orElse(null);

        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(ROLE, user.getRoles().get(0).getName());
    }

    @Test
    public void testCreateUser() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setPassword("TestUser1234");

        String responseJson = mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(responseJson, UserDTO.class);

        assertNotNull(createdUser);
        assertEquals(createUserDTO.getEmail(), createdUser.getEmail());
    }


    @Test
    public void testUpdateUser() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test2@example.com");
        createUserDTO.setPassword("TestUser1234");

        String createUserResponseJson = mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(createUserResponseJson, UserDTO.class);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail("updated@example.com");

        String updateUserResponseJson = mockMvc.perform(put("/api/user/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO updatedUser = objectMapper.readValue(updateUserResponseJson, UserDTO.class);

        assertNotNull(updatedUser);
        assertEquals(updateUserDTO.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testGetUser() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test4@example.com");
        createUserDTO.setPassword("TestUser1234");

        String createUserResponseJson = mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(createUserResponseJson, UserDTO.class);

        String getUserResponseJson = mockMvc.perform(get("/api/user/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO fetchedUser = objectMapper.readValue(getUserResponseJson, UserDTO.class);

        assertNotNull(fetchedUser);
        assertEquals(createdUser.getId(), fetchedUser.getId());
        assertEquals(createdUser.getEmail(), fetchedUser.getEmail());
    }


    @Test
    public void testGetAllUsers() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        mockMvc.perform(get("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].email").value(EMAIL));
    }


    @Test
    public void testDeleteUser() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test3@example.com");
        createUserDTO.setPassword("TestUser1234");

        String createUserResponseJson = mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(createUserResponseJson, UserDTO.class);

        mockMvc.perform(delete("/api/user/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/user/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddUserRole() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test5@example.com");
        createUserDTO.setPassword("TestUser1234");

        String createUserResponseJson = mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(createUserResponseJson, UserDTO.class);

        Role role = new Role();
        role.setName("ROLE_TEST");
        role.setDescription("User role");
        Role savedRole = roleRepository.save(role);

        String addUserRoleResponseJson = mockMvc.perform(post("/api/user/" + createdUser.getId() + "/roles/" + savedRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserWithRoleDTO userWithAddedRole = objectMapper.readValue(addUserRoleResponseJson, UserWithRoleDTO.class);

        assertNotNull(userWithAddedRole);
        assertEquals(createdUser.getId(), userWithAddedRole.getId());
        assertEquals(createdUser.getEmail(), userWithAddedRole.getEmail());
        assertEquals(2, userWithAddedRole.getRoles().size());
        assertEquals(savedRole.getId(), userWithAddedRole.getRoles().get(1).getId());
        assertEquals(savedRole.getName(), userWithAddedRole.getRoles().get(1).getName());
    }

    @Test
    public void testRemoveUserRole() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test6@example.com");
        createUserDTO.setPassword("TestUser1234");

        String createUserResponseJson = mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDTO createdUser = objectMapper.readValue(createUserResponseJson, UserDTO.class);

        Role role = new Role();
        role.setName("ROLE_TEST1");
        role.setDescription("User role");
        Role savedRole = roleRepository.save(role);

        mockMvc.perform(post("/api/user/" + createdUser.getId() + "/roles/" + savedRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk());

        String removeUserRoleResponseJson = mockMvc.perform(delete("/api/user/" + createdUser.getId() + "/roles/" + savedRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserWithRoleDTO userWithRemovedRole = objectMapper.readValue(removeUserRoleResponseJson, UserWithRoleDTO.class);

        assertNotNull(userWithRemovedRole);
        assertEquals(createdUser.getId(), userWithRemovedRole.getId());
        assertEquals(createdUser.getEmail(), userWithRemovedRole.getEmail());
        assertEquals(1, userWithRemovedRole.getRoles().size());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}
