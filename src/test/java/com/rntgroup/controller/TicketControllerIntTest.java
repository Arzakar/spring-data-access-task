package com.rntgroup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestDataUtil;
import com.rntgroup.db.EventDatabase;
import com.rntgroup.db.TicketDatabase;
import com.rntgroup.db.UserDatabase;
import com.rntgroup.dto.BookTicketRequestDto;
import com.rntgroup.enumerate.Category;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
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
class TicketControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventDatabase eventDatabase;

    @Autowired
    private TicketDatabase ticketDatabase;

    @Autowired
    private UserDatabase userDatabase;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        eventDatabase.getData().clear();
        ticketDatabase.getData().clear();
        userDatabase.getData().clear();
    }

    @Test
    @SneakyThrows
    void shouldBookTicket() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());
        User user = userDatabase.insert(TestDataUtil.getRandomUser());
        var bookTicketRequestDto = new BookTicketRequestDto()
                .setEventId(event.getId())
                .setUserId(user.getId())
                .setPlace(0)
                .setCategory(Category.STANDARD);

        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tickets")
                        .content(objectMapper.writeValueAsString(bookTicketRequestDto)))
                .andExpect(status().isOk());

        assertEquals(1, ticketDatabase.getData().size());

        Ticket bookedTicket = ticketDatabase.getData().values().stream().findFirst().orElseThrow();
        result.andExpect(content().json(objectMapper.writeValueAsString(bookedTicket)));
    }

    @Test
    @SneakyThrows
    void shouldReturnTicketByEventId() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());
        User user = userDatabase.insert(TestDataUtil.getRandomUser());
        Ticket ticket = ticketDatabase.insert(TestDataUtil.getRandomBookedTicket(event.getId(), user.getId()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets?eventId=" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(ticket))));
    }

    @Test
    @SneakyThrows
    void shouldReturnTicketByUserId() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());
        User user = userDatabase.insert(TestDataUtil.getRandomUser());
        Ticket ticket = ticketDatabase.insert(TestDataUtil.getRandomBookedTicket(event.getId(), user.getId()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets?userId=" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(ticket))));
    }

    @Test
    @SneakyThrows
    void shouldCancelTicket() {
        Event event = eventDatabase.insert(TestDataUtil.getRandomEvent());
        User user = userDatabase.insert(TestDataUtil.getRandomUser());
        Ticket ticket = ticketDatabase.insert(TestDataUtil.getRandomBookedTicket(event.getId(), user.getId()));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tickets/" + ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets?eventId=0&userId=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotImplement() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotImplemented());
    }
}