package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.dto.BookTicketRequestDto;
import com.rntgroup.enumerate.Category;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.repository.EventRepository;
import com.rntgroup.repository.TicketRepository;
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

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldBookTicket() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());
        User user = userRepository.save(TestDataUtil.getRandomUser());
        var bookTicketRequestDto = new BookTicketRequestDto()
                .setEventId(event.getId())
                .setUserId(user.getId())
                .setPlace(0)
                .setCategory(Category.STANDARD);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tickets")
                        .content(objectMapper.writeValueAsString(bookTicketRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, ticketRepository.findAll().size());

        Ticket bookedTicket = ticketRepository.findAll().stream().findFirst().orElseThrow();
        result.andExpect(content().json(objectMapper.writeValueAsString(bookedTicket)));
    }

    @Test
    @SneakyThrows
    void shouldReturnTicketByEventId() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());
        User user = userRepository.save(TestDataUtil.getRandomUser());
        Ticket ticket = ticketRepository.save(TestDataUtil.getRandomBookedTicket(event, user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets/search?eventId=" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(ticket))));
    }

    @Test
    @SneakyThrows
    void shouldReturnTicketByUserId() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());
        User user = userRepository.save(TestDataUtil.getRandomUser());
        Ticket ticket = ticketRepository.save(TestDataUtil.getRandomBookedTicket(event, user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets/search?userId=" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(ticket))));
    }

    @Test
    @SneakyThrows
    void shouldCancelTicket() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());
        User user = userRepository.save(TestDataUtil.getRandomUser());
        Ticket ticket = ticketRepository.save(TestDataUtil.getRandomBookedTicket(event, user));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tickets/" + ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequest() {
        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets/search?eventId="+ id + "&userId=" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotImplement() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotImplemented());
    }
}