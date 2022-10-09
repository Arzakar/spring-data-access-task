package com.rntgroup.service;

import static com.rntgroup.TestDataUtil.DEFAULT_PAGE_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rntgroup.TestDataUtil;
import com.rntgroup.exception.BadRequestException;
import com.rntgroup.exception.ValidationException;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.repository.TicketRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("Должен сохранить Ticket в БД и вернуть сохранённый объект")
    void shouldCreateTicket() {
        Event event = TestDataUtil.getRandomEvent(UUID.randomUUID());
        Ticket ticket = TestDataUtil.getRandomTicket().setEvent(event);

        when(ticketRepository.findByEventIdAndPlace(any(UUID.class), any(Integer.class))).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        assertEquals(ticket, ticketService.create(ticket));
        verify(ticketRepository).findByEventIdAndPlace(event.getId(), ticket.getPlace());
        verify(ticketRepository).save(ticket);
    }

    @Test
    @DisplayName("Должен выбросить ошибку, т.к. на этот Event заданное место уже занято")
    void shouldThrowExceptionBecauseThisPlaceAlreadyExist() {
        Event event = TestDataUtil.getRandomEvent(UUID.randomUUID());
        Ticket ticket = TestDataUtil.getRandomTicket().setEvent(event);

        ValidationException expectedException = new ValidationException(String.format("Ticket with place = %d already exist", ticket.getPlace()));

        when(ticketRepository.findByEventIdAndPlace(any(UUID.class), any(Integer.class))).thenReturn(Optional.of(ticket));

        ValidationException thrown = assertThrows(ValidationException.class, () -> ticketService.create(ticket));
        assertEquals(expectedException.getMessage(), thrown.getMessage());
        verify(ticketRepository).findByEventIdAndPlace(event.getId(), ticket.getPlace());
        verify(ticketRepository, never()).save(ticket);
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Tickets с одинаковыми eventId с определённой страницы")
    void shouldReturnTicketsByEvent() {
        Event event = TestDataUtil.getRandomEvent(UUID.randomUUID());
        List<Ticket> tickets = List.of(
                TestDataUtil.getRandomTicket().setEvent(event),
                TestDataUtil.getRandomTicket().setEvent(event)
        );

        when(ticketRepository.findByEventId(any(UUID.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(tickets, DEFAULT_PAGE_REQUEST, tickets.size()));

        assertEquals(tickets, ticketService.findByEventId(event.getId(), DEFAULT_PAGE_REQUEST));
        verify(ticketRepository).findByEventId(event.getId(), DEFAULT_PAGE_REQUEST);
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Tickets с одинаковыми userId с определённой страницы")
    void shouldReturnTicketsByUser() {
        User user = TestDataUtil.getRandomUser(UUID.randomUUID());
        List<Ticket> tickets = List.of(
                TestDataUtil.getRandomTicket().setUser(user),
                TestDataUtil.getRandomTicket().setUser(user)
        );

        when(ticketRepository.findByUserId(any(UUID.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(tickets, DEFAULT_PAGE_REQUEST, tickets.size()));

        assertEquals(tickets, ticketService.findByUserId(user.getId(), DEFAULT_PAGE_REQUEST));
        verify(ticketRepository).findByUserId(user.getId(), DEFAULT_PAGE_REQUEST);
    }

    @Test
    @DisplayName("Должен удалить Ticket из БД и вернуть удалённый объект")
    void shouldDeleteTicketById() {
        UUID id = UUID.randomUUID();

        when(ticketRepository.existsById(id)).thenReturn(false);

        assertTrue(ticketService.deleteById(id));
        verify(ticketRepository).deleteById(id);
    }
}