package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EventControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldReturnEventById() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(event)));
    }

    @Test
    @SneakyThrows
    void shouldReturnEventByTitle() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search?title=" + event.getTitle())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(event))));
    }

    @Test
    @SneakyThrows
    void shouldReturnEventByDate() {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-10-10");
        Event event = eventRepository.save(TestDataUtil.getRandomEvent().setDate(date));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search?day=2022-10-10")
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

        assertEquals(1, eventRepository.findAll().size());

        Event savedEvent = eventRepository.findAll().stream().findFirst().orElseThrow();
        result.andExpect(content().json(objectMapper.writeValueAsString(savedEvent)));
    }

    @Test
    @SneakyThrows
    void shouldUpdateEvent() {
        Event savedEvent = eventRepository.save(TestDataUtil.getRandomEvent());
        Event eventForUpdate = TestUtil.deepCopy(savedEvent);
        eventForUpdate.setTitle("TestName");

        String eventForUpdateAsString = objectMapper.writeValueAsString(eventForUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/events")
                        .content(eventForUpdateAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(eventForUpdateAsString));

        assertEquals(1, eventRepository.findAll().size());
        assertEquals(eventForUpdate, eventRepository.findAll().stream().findFirst().orElseThrow());
    }

    @Test
    @SneakyThrows
    void shouldDeleteEvent() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertTrue(eventRepository.findAll().isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundException() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search?title=Test&day=2022-10-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search?day=2022/10/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotImplement() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search")
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