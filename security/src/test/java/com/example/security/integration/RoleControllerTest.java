package com.example.security.integration;

import com.example.security.dtos.auth.input.AuthenticationRequestDTO;
import com.example.security.dtos.auth.output.AuthenticationResponseDTO;
import com.example.security.dtos.role.input.CreateRoleDTO;
import com.example.security.dtos.role.input.UpdateRoleDTO;
import com.example.security.dtos.role.output.RoleDTO;
import com.example.security.dtos.role.output.RoleWithUsersDTO;
import com.example.security.dtos.user.input.CreateUserDTO;
import com.example.security.dtos.user.output.UserDTO;
import com.example.security.entitys.Role;
import com.example.security.entitys.User;
import com.example.security.repositories.RoleRepo;
import com.example.security.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class RoleControllerTest {


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
    public void testCreateRole() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateRoleDTO createRoleDTO = new CreateRoleDTO("ROLE_TEST", "Test role description");

        String responseJson = mockMvc.perform(post("/api/roles")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoleDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RoleDTO createdRole = objectMapper.readValue(responseJson, RoleDTO.class);

        assertNotNull(createdRole);
        assertEquals(createRoleDTO.getName(), createdRole.getName());
        assertEquals(createRoleDTO.getDescription(), createdRole.getDescription());
    }

    @Test
    public void testUpdateRole() throws Exception {
        var authenticationResponseDTO = obtainAccess();
        CreateRoleDTO createRoleDTO = new CreateRoleDTO("ROLE_TEST_UPDATE", "Test role for update");
        String createRoleResponseJson = mockMvc.perform(post("/api/roles")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoleDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RoleDTO createdRole = objectMapper.readValue(createRoleResponseJson, RoleDTO.class);


        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO("ROLE_TEST_UPDATED", "Updated test role description");
        String updateRoleResponseJson = mockMvc.perform(put("/api/roles/" + createdRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRoleDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleDTO updatedRole = objectMapper.readValue(updateRoleResponseJson, RoleDTO.class);
        assertNotNull(updatedRole);
        assertEquals(createdRole.getId(), updatedRole.getId());
        assertEquals(updateRoleDTO.getName(), updatedRole.getName());
        assertEquals(updateRoleDTO.getDescription(), updatedRole.getDescription());
    }


    @Test
    public void testGetRole() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        Role role = new Role();
        role.setName("ROLE_TEST_GET");
        role.setDescription("Test role for get");
        Role savedRole = roleRepository.save(role);

        String getRoleResponseJson = mockMvc.perform(get("/api/roles/" + savedRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleDTO fetchedRole = objectMapper.readValue(getRoleResponseJson, RoleDTO.class);

        assertNotNull(fetchedRole);
        assertEquals(savedRole.getId(), fetchedRole.getId());
        assertEquals(savedRole.getName(), fetchedRole.getName());
        assertEquals(savedRole.getDescription(), fetchedRole.getDescription());
    }

    @Test
    public void testGetAllRoles() throws Exception {
        // Отримати доступ та токен
        var authenticationResponseDTO = obtainAccess();

        mockMvc.perform(get("/api/roles")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(ROLE));
    }

    @Test
    public void testDeleteRole() throws Exception {
        // Отримати доступ та токен
        var authenticationResponseDTO = obtainAccess();

        // Створити нову роль
        CreateRoleDTO createRoleDTO = new CreateRoleDTO();
        createRoleDTO.setName("TestRole");

        String createRoleResponseJson = mockMvc.perform(post("/api/roles")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoleDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RoleDTO createdRole = objectMapper.readValue(createRoleResponseJson, RoleDTO.class);

        // Виконати запит DELETE на видалення ролі
        mockMvc.perform(delete("/api/roles/" + createdRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isNoContent());

        // Перевірити, що роль дійсно була видалена
        mockMvc.perform(get("/api/roles/" + createdRole.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddRoleToUser() throws Exception {
        // Отримати доступ та токен
        var authenticationResponseDTO = obtainAccess();

        // Створити нового користувача
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

        // Створити нову роль
        Role role = new Role();
        role.setName("ROLE_TEST");
        role.setDescription("User role");
        Role savedRole = roleRepository.save(role);

        // Додати роль до користувача
        String addRoleToUserResponseJson = mockMvc.perform(post("/api/roles/" + savedRole.getId() + "/users/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleWithUsersDTO roleWithAddedUser = objectMapper.readValue(addRoleToUserResponseJson, RoleWithUsersDTO.class);

        assertNotNull(roleWithAddedUser);
        assertEquals(savedRole.getId(), roleWithAddedUser.getId());
        assertEquals(savedRole.getName(), roleWithAddedUser.getName());
        assertEquals(1, roleWithAddedUser.getUsers().size());
        assertEquals(createdUser.getId(), roleWithAddedUser.getUsers().get(0).getId());
        assertEquals(createdUser.getEmail(), roleWithAddedUser.getUsers().get(0).getEmail());
    }

    @Test
    public void testRemoveUserRole() throws Exception {
        // Отримати доступ та токен
        var authenticationResponseDTO = obtainAccess();

        // Створити нового користувача
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

        // Створити нову роль та додати її до користувача
        Role role = new Role();
        role.setName("ROLE_TEST1");
        role.setDescription("User role");
        Role savedRole = roleRepository.save(role);

        mockMvc.perform(post("/api/roles/" + savedRole.getId() + "/users/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk());

        // Видалити роль у користувача
        String removeUserRoleResponseJson = mockMvc.perform(delete("/api/roles/" + savedRole.getId() + "/users/" + createdUser.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleWithUsersDTO roleWithRemovedUser = objectMapper.readValue(removeUserRoleResponseJson, RoleWithUsersDTO.class);

        // Перевірити, що роль була видалена у користувача
        assertNotNull(roleWithRemovedUser);
        assertEquals(savedRole.getId(), roleWithRemovedUser.getId());
        assertEquals(savedRole.getName(), roleWithRemovedUser.getName());
        assertEquals(0, roleWithRemovedUser.getUsers().size());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}
