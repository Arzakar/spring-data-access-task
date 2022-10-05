package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldReturnUserById() {
        User user = userRepository.save(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    @SneakyThrows
    void shouldReturnUserByName() {
        User user = userRepository.save(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search?name=" + user.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }

    @Test
    @SneakyThrows
    void shouldReturnUserByEmail() {
        User user = userRepository.save(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search?email=" + user.getEmail())
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

        assertEquals(1, userRepository.findAll().size());

        User savedUser = userRepository.findAll().stream().findFirst().orElseThrow();
        result.andExpect(content().json(objectMapper.writeValueAsString(savedUser)));
    }

    @Test
    @SneakyThrows
    void shouldUpdateUser() {
        User savedUser = userRepository.save(TestDataUtil.getRandomUser());
        User userForUpdate = TestUtil.deepCopy(savedUser);
        userForUpdate.setName("TestName");

        String userForUpdateAsString = objectMapper.writeValueAsString(userForUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users")
                        .content(userForUpdateAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userForUpdateAsString));

        assertEquals(1, userRepository.findAll().size());
        assertEquals(userForUpdate, userRepository.findAll().stream().findFirst().orElseThrow());
    }

    @Test
    @SneakyThrows
    void shouldDeleteUser() {
        User user = userRepository.save(TestDataUtil.getRandomUser());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundException() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search?name=TestName&email=TestEmail")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotImplement() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search")
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