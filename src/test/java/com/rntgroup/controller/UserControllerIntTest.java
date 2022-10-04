package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.db.UserDatabase;
import com.rntgroup.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDatabase userDatabase;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userDatabase.getData().clear();
    }

    @Test
    @SneakyThrows
    void shouldReturnUserById() {
        User user = userDatabase.insert(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    @SneakyThrows
    void shouldReturnUserByName() {
        User user = userDatabase.insert(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users?name=" + user.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }

    @Test
    @SneakyThrows
    void shouldReturnUserByEmail() {
        User user = userDatabase.insert(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users?email=" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }

    @Test
    @SneakyThrows
    void shouldCreateUser() {
        User userForSave = TestDataUtil.getRandomUser();
        String userForSaveAsString = objectMapper.writeValueAsString(userForSave);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(userForSaveAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, userDatabase.getData().size());

        User savedUser = userDatabase.getData().values().stream().findFirst().orElseThrow();
        result.andExpect(content().json(objectMapper.writeValueAsString(savedUser)));
    }

    @Test
    @SneakyThrows
    void shouldUpdateUser() {
        User savedUser = userDatabase.insert(TestDataUtil.getRandomUser());
        User userForUpdate = TestUtil.deepCopy(savedUser);
        userForUpdate.setName("TestName");

        String userForUpdateAsString = objectMapper.writeValueAsString(userForUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users")
                        .content(userForUpdateAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userForUpdateAsString));

        assertEquals(1, userDatabase.getData().size());
        assertEquals(userForUpdate, userDatabase.getData().values().stream().findFirst().orElseThrow());
    }

    @Test
    @SneakyThrows
    void shouldDeleteUser() {
        User user = userDatabase.insert(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertTrue(userDatabase.getData().isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundException() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users?name=TestName&email=TestEmail")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotImplement() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotImplemented());
    }

    @Test
    @SneakyThrows
    void shouldReturnInternalServerError() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}