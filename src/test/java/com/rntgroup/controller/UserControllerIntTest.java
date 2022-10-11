package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.dto.UserDto;
import com.rntgroup.mapper.UserMapper;
import com.rntgroup.model.User;
import com.rntgroup.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldCreateUser() {
        UserDto userForSave = TestDataUtil.getRandomUserDto();
        String userForSaveAsString = objectMapper.writeValueAsString(userForSave);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(userForSaveAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, userRepository.findAll().size());

        User savedUser = userRepository.findAll().stream().findFirst().orElseThrow();
        UserDto savedUserDto = userMapper.toDto(savedUser);
        result.andExpect(content().json(objectMapper.writeValueAsString(savedUserDto)));
    }

    @Test
    @SneakyThrows
    void shouldReturnUserById() {
        User user = userRepository.save(TestDataUtil.getRandomUser());
        UserDto userDto = userMapper.toDto(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @SneakyThrows
    void shouldUpdateUserById() {
        User savedUser = userRepository.save(TestDataUtil.getRandomUser());
        User userForUpdate = TestUtil.deepCopy(savedUser).setName("TestName");

        UserDto userForUpdateDto = userMapper.toDto(userForUpdate);
        String userForUpdateDtoAsString = objectMapper.writeValueAsString(userForUpdateDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/" + userForUpdateDto.getId())
                        .content(userForUpdateDtoAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userForUpdateDtoAsString));

        assertEquals(1, userRepository.findAll().size());
        assertEquals(userForUpdate, userRepository.findAll().stream().findFirst().orElseThrow());
    }

    @Test
    @SneakyThrows
    void shouldDeleteUserById() {
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
    void shouldReturnUserByName() {
        String sameName = "Name";

        List<User> users = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            users.add(userRepository.save(TestDataUtil.getRandomUser().setName(sameName)));
        }

        List<UserDto> userDtoList = users.stream()
                .map(userMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search?name=" + sameName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDtoList)));
    }

    @Test
    @SneakyThrows
    void shouldReturnUserByEmail() {
        String testEmail = "testEmail";
        User specialUser = userRepository.save(TestDataUtil.getRandomUser().setEmail(testEmail));

        userRepository.save(TestDataUtil.getRandomUser());
        userRepository.save(TestDataUtil.getRandomUser());
        userRepository.save(TestDataUtil.getRandomUser());
        userRepository.save(TestDataUtil.getRandomUser());

        List<UserDto> specialUserDtoList = Collections.singletonList(userMapper.toDto(specialUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search?email=" + testEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(specialUserDtoList)));
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

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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