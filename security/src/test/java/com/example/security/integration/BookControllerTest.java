package com.example.security.integration;

import com.example.security.dtos.auth.input.AuthenticationRequestDTO;
import com.example.security.dtos.auth.input.RegistrationRequestDTO;
import com.example.security.dtos.auth.output.AuthenticationResponseDTO;
import com.example.security.dtos.book.input.CreateBookDTO;
import com.example.security.dtos.book.input.UpdateBookDTO;
import com.example.security.dtos.book.output.BookWithAuthorsDTO;
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
import com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

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

    private AuthenticationResponseDTO obtainAccess() throws Exception {
        String email = "book" + System.currentTimeMillis() + "@example.com";
        RegistrationRequestDTO registrationRequestDTO = new RegistrationRequestDTO();
        registrationRequestDTO.setEmail(email);
        registrationRequestDTO.setPassword("Book1234");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isCreated());

        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail(email);
        authenticationRequestDTO.setPassword("Book1234");

        String authResponseJson = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(authResponseJson, AuthenticationResponseDTO.class);
    }

    @Test
    public void testCreateBook() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateBookDTO createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Test Book");
        createBookDTO.setPrice("19.99");
        createBookDTO.setAuthorId(authenticationResponseDTO.getUser().getId());

        String responseJson = mockMvc.perform(post("/api/book")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookWithAuthorsDTO createdBook = objectMapper.readValue(responseJson, BookWithAuthorsDTO.class);

        assertNotNull(createdBook);
        assertEquals(createBookDTO.getTitle(), createdBook.getTitle());
        assertEquals(createBookDTO.getPrice(), createdBook.getPrice());
        assertEquals(createBookDTO.getAuthorId(), createdBook.getAuthor().getId());
    }

    @Test
    public void testUpdateBook() throws Exception {
        var authenticationResponse = obtainAccess();
        CreateBookDTO createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Test Book");
        createBookDTO.setPrice("19.99");
        createBookDTO.setAuthorId(authenticationResponse.getUser().getId());

        String createResponseJson = mockMvc.perform(post("/api/book")
                        .header("Authorization", "Bearer " + authenticationResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookWithAuthorsDTO createdBook = objectMapper.readValue(createResponseJson, BookWithAuthorsDTO.class);


        UpdateBookDTO updateBookDTO = new UpdateBookDTO();
        updateBookDTO.setTitle("Updated Test Book");
        updateBookDTO.setPrice("24.99");
        updateBookDTO.setAuthorId(authenticationResponse.getUser().getId());

        String updateResponseJson = mockMvc.perform(put("/api/book/" + createdBook.getId())
                        .header("Authorization", "Bearer " + authenticationResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookWithAuthorsDTO updatedBook = objectMapper.readValue(updateResponseJson, BookWithAuthorsDTO.class);

        assertNotNull(updatedBook);
        assertEquals(updateBookDTO.getTitle(), updatedBook.getTitle());
        assertEquals(updateBookDTO.getPrice(), updatedBook.getPrice());
        assertEquals(updateBookDTO.getAuthorId(), updatedBook.getAuthor().getId());
    }

    @Test
    public void testGetBook() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateBookDTO createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Test Book");
        createBookDTO.setPrice("19.99");
        createBookDTO.setAuthorId(authenticationResponseDTO.getUser().getId());

        String createResponseJson = mockMvc.perform(post("/api/book")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookWithAuthorsDTO createdBook = objectMapper.readValue(createResponseJson, BookWithAuthorsDTO.class);

        String getResponseJson = mockMvc.perform(get("/api/book/" + createdBook.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookWithAuthorsDTO fetchedBook = objectMapper.readValue(getResponseJson, BookWithAuthorsDTO.class);

        assertNotNull(fetchedBook);
        assertEquals(createdBook.getId(), fetchedBook.getId());
        assertEquals(createdBook.getTitle(), fetchedBook.getTitle());
        assertEquals(createdBook.getPrice(), fetchedBook.getPrice());
        assertEquals(createdBook.getAuthor().getId(), fetchedBook.getAuthor().getId());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateBookDTO createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Test Book");
        createBookDTO.setPrice("19.99");
        createBookDTO.setAuthorId(authenticationResponseDTO.getUser().getId());

        mockMvc.perform(post("/api/book")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDTO)))
                .andExpect(status().isCreated());

        String getAllResponseJson = mockMvc.perform(get("/api/book")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BookWithAuthorsDTO> fetchedBooks = objectMapper.readValue(getAllResponseJson, new TypeReference<>() {
        });

        assertNotNull(fetchedBooks);
        assertEquals(1, fetchedBooks.size());
    }

    @Test
    public void testDeleteBook() throws Exception {
        var authenticationResponseDTO = obtainAccess();

        CreateBookDTO createBookDTO = new CreateBookDTO();
        createBookDTO.setTitle("Test Book");
        createBookDTO.setPrice("19.99");
        createBookDTO.setAuthorId(authenticationResponseDTO.getUser().getId());

        String createResponseJson = mockMvc.perform(post("/api/book")
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookWithAuthorsDTO createdBook = objectMapper.readValue(createResponseJson, BookWithAuthorsDTO.class);

        mockMvc.perform(delete("/api/book/" + createdBook.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/book/" + createdBook.getId())
                        .header("Authorization", "Bearer " + authenticationResponseDTO.getAccessToken()))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}