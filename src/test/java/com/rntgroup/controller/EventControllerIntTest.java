package com.rntgroup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.db.EventDatabase;
import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class EventControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventController eventController;

    @Autowired
    private EventDatabase eventDatabase;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        eventDatabase.getData().clear();
    }

    @Test
    @SneakyThrows
    void shouldReturnEventById() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(event)));
    }

    @Test
    @SneakyThrows
    void shouldReturnEventByTitle() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events?title=" + event.getTitle())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(event))));
    }

    @Test
    @SneakyThrows
    void shouldReturnEventByDate() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());
        event.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-10-10"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events?day=2022-10-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(event))));
    }

    @Test
    @SneakyThrows
    void shouldCreateEvent() {
        Event eventForSave = TestDataUtil.getRandomEvent();
        String eventForSaveAsString = objectMapper.writeValueAsString(eventForSave);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/events")
                        .content(eventForSaveAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, eventDatabase.getData().size());

        Event savedEvent = eventDatabase.getData().values().stream().findFirst().orElseThrow();
        result.andExpect(content().json(objectMapper.writeValueAsString(savedEvent)));
    }

    @Test
    @SneakyThrows
    void shouldUpdateEvent() {
        Event savedEvent = eventDatabase.insert(TestDataUtil.getRandomEvent());
        Event eventForUpdate = TestUtil.deepCopy(savedEvent);
        eventForUpdate.setTitle("TestName");

        String eventForUpdateAsString = objectMapper.writeValueAsString(eventForUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/events")
                        .content(eventForUpdateAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(eventForUpdateAsString));

        assertEquals(1, eventDatabase.getData().size());
        assertEquals(eventForUpdate, eventDatabase.getData().values().stream().findFirst().orElseThrow());
    }

    @Test
    @SneakyThrows
    void shouldDeleteEvent() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertTrue(eventDatabase.getData().isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundException() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events?title=Test&day=2022-10-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events?day=2022/10/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotImplement() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotImplemented());
    }

    @Test
    @SneakyThrows
    void shouldReturnInternalServerError() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}