package com.example.nosqldb.integrations;

import com.example.nosqldb.configs.MongoTestConfiguration;
import com.example.nosqldb.documents.User;
import com.example.nosqldb.dtos.user.input.UserCreateDTO;
import com.example.nosqldb.dtos.user.input.UserUpdateDTO;
import com.example.nosqldb.dtos.user.output.UserDTO;
import com.example.nosqldb.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MongoTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User("asdfasdfqwet", "John", "Doe", "john.doe@example.com", 25, false);
        userDTO = new UserDTO(user.getId(), "John", "Doe", "john.doe@example.com", 25, false);
    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void createUserInvalidRequestTest() throws Exception {
        UserCreateDTO invalidUser = new UserCreateDTO("", "", "invalidemail", 25, false);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.email").value("Email should be valid"));
    }


    @Test
    void createUserTest() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(userDTO.getAge()))
                .andExpect(jsonPath("$.married").value(userDTO.isMarried()));

        var userFromDb = userRepo.findByEmail(user.getEmail());
        Assertions.assertThat(userFromDb).isNotNull();
        Assertions.assertThat(userFromDb.getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    void createUserWithExistingEmailTest() throws Exception {
        userRepo.save(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }


    @Test
    void getUserByEmailNotFoundTest() throws Exception {
        mockMvc.perform(get("/api/users/email/{email}", "notfound.email@example.com"))
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllUsersTest() throws Exception {
        userRepo.save(user);

        String response = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response: " + response);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$[0].email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$[0].age").value(userDTO.getAge()))
                .andExpect(jsonPath("$[0].married").value(userDTO.isMarried()));
    }


    @Test
    void getUserByEmailTest() throws Exception {
        userRepo.save(user);

        mockMvc.perform(get("/api/users/email/{email}", user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(userDTO.getAge()))
                .andExpect(jsonPath("$.married").value(userDTO.isMarried()));
    }

    @Test
    void updateUserTest() throws Exception {
        User savedUser = userRepo.save(user);
        UserUpdateDTO updatedUser = new UserUpdateDTO("Updated", "User", "updated.user@example.com", 30, true);

        mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()))
                .andExpect(jsonPath("$.married").value(updatedUser.isMarried()));
    }

    @Test
    void updateUserNotFoundTest() throws Exception {
        UserUpdateDTO updatedUser = new UserUpdateDTO("Updated", "User", "updated.user@example.com", 30, true);

        mockMvc.perform(put("/api/users/{id}", "asdfasdfqwet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByFirstNameTest() throws Exception {
        userRepo.save(user);

        mockMvc.perform(get("/api/users/firstname/{firstName}", user.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value(userDTO.getFirstName()));
    }

    @Test
    void getUsersByLastNameTest() throws Exception {
        userRepo.save(user);

        mockMvc.perform(get("/api/users/lastname/{lastName}", user.getLastName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].lastName").value(userDTO.getLastName()));
    }

    @Test
    void getUsersOlderThanTest() throws Exception {
        userRepo.save(user);

        mockMvc.perform(get("/api/users/olderthan/{age}", user.getAge() - 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].age").value(userDTO.getAge()));
    }

    @Test
    void getMarriedUsersTest() throws Exception {
        userRepo.save(user);

        mockMvc.perform(get("/api/users/married/{isMarried}", user.isMarried()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].married").value(userDTO.isMarried()));
    }


    @Test
    void deleteUserTest() throws Exception {
        User savedUser = userRepo.save(user);

        mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertThat(userRepo.findById(savedUser.getId())).isEmpty();
    }

    @Test
    void deleteUserNotFoundTest() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", "asdfasdfqwet"))
                .andExpect(status().isNotFound());
    }

}
