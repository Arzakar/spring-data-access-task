package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.dto.EventDto;
import com.rntgroup.mapper.EventMapper;
import com.rntgroup.model.Event;

import com.rntgroup.repository.EventRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EventControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldCreateEvent() {
        EventDto eventForSave = TestDataUtil.getRandomEventDto();
        String eventForSaveAsString = objectMapper.writeValueAsString(eventForSave);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/events")
                        .content(eventForSaveAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, eventRepository.findAll().size());

        Event savedEvent = eventRepository.findAll().stream().findFirst().orElseThrow();
        EventDto savedEventDto = eventMapper.toDto(savedEvent);
        result.andExpect(content().json(objectMapper.writeValueAsString(savedEventDto)));
    }

    @Test
    @SneakyThrows
    void shouldReturnEventById() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());
        EventDto eventDto = eventMapper.toDto(event);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + event.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(eventDto)));
    }

    @Test
    @SneakyThrows
    void shouldUpdateEventById() {
        Event savedEvent = eventRepository.save(TestDataUtil.getRandomEvent());
        Event eventForUpdate =  TestUtil.deepCopy(savedEvent).setTitle("Test Title");

        EventDto eventForUpdateDto = eventMapper.toDto(eventForUpdate);
        String eventForUpdateDtoAsString = objectMapper.writeValueAsString(eventForUpdateDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/events/" + eventForUpdateDto.getId())
                        .content(eventForUpdateDtoAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(eventForUpdateDtoAsString));

        assertEquals(1, eventRepository.findAll().size());
        assertEquals(eventForUpdate, eventRepository.findAll().stream().findFirst().orElseThrow());
    }

    @Test
    @SneakyThrows
    void shouldDeleteEventById() {
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
    void shouldReturnEventByTitle() {
        String sameTitle = "Same Title";

        List<Event> events = List.of(
                eventRepository.save(TestDataUtil.getRandomEvent().setTitle(sameTitle)),
                eventRepository.save(TestDataUtil.getRandomEvent().setTitle(sameTitle)),
                eventRepository.save(TestDataUtil.getRandomEvent().setTitle(sameTitle)),
                eventRepository.save(TestDataUtil.getRandomEvent().setTitle(sameTitle)),
                eventRepository.save(TestDataUtil.getRandomEvent().setTitle(sameTitle))
        );

        List<EventDto> eventDtoList = events.stream()
                .map(eventMapper::toDto)
                .sorted(Comparator.comparing(EventDto::getId))
                .collect(Collectors.toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search?title=" + sameTitle)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(eventDtoList)));
    }

    @Test
    @SneakyThrows
    void shouldReturnEventByDate() {
        LocalDate sameDate = LocalDate.now();

        List<Event> events = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            events.add(eventRepository.save(TestDataUtil.getRandomEvent().setDate(sameDate)));
        }

        List<EventDto> eventDtoList = events.stream()
                .map(eventMapper::toDto)
                .sorted(Comparator.comparing(EventDto::getId))
                .collect(Collectors.toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search?day=" + sameDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(eventDtoList)));
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
                        .get("/events/search?title=Test&day=" + LocalDate.now())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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