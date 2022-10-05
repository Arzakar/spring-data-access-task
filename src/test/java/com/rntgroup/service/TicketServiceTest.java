package com.rntgroup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rntgroup.enumerate.Category;
import com.rntgroup.exception.BadRequestException;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.repository.TicketRepository;

import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;
import com.rntgroup.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("Должен сохранить Ticket в БД и вернуть сохранённый объект")
    void shouldCreateTicket() {
        Ticket testTicket = new Ticket(0L, 0L, 0L, Category.BAR, 0);

        when(ticketRepository.existByPlace(any(Ticket.class))).thenReturn(false);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        assertEquals(testTicket, ticketService.create(testTicket));
        verify(ticketRepository).existByPlace(testTicket);
        verify(ticketRepository).save(testTicket);
    }

    @Test
    @DisplayName("Должен выбросить ошибку, т.к. на этот Event заданное место уже занято")
    void shouldThrowExceptionBecauseThisPlaceAlreadyExist() {
        BadRequestException expectedException = new BadRequestException("Ticket with place = 0 already exist");

        Ticket testTicket = new Ticket(0L, 0L, 0L, Category.BAR, 0);

        when(ticketRepository.existByPlace(any(Ticket.class))).thenReturn(true);

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> ticketService.create(testTicket));
        assertEquals(expectedException.getMessage(), thrown.getMessage());
        verify(ticketRepository).existByPlace(testTicket);
        verify(ticketRepository, never()).save(testTicket);
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Tickets с одинаковыми eventId с определённой страницы")
    void shouldReturnTicketsByEvent() {
        Event testEvent = new Event(0L, "TestEvent", new Date());
        List<Ticket> testTickets = List.of(new Ticket(0L, 0L, 0L, Category.BAR, 0),
                new Ticket(1L, 0L, 1L, Category.BAR, 1));

        when(ticketRepository.findByEventId(any(Long.class), any(Page.class)))
                .thenReturn(new SearchResult<Ticket>().setContent(testTickets).setPage(Page.of(2, 1)));

        assertEquals(testTickets, ticketService.findByEvent(testEvent, 2, 1));
        verify(ticketRepository).findByEventId(0L, Page.of(2, 1));
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Tickets с одинаковыми userId с определённой страницы")
    void shouldReturnTicketsByUser() {
        User testUser = new User(0L, "TestUser", "test.email@test.com");
        List<Ticket> testTickets = List.of(new Ticket(0L, 0L, 0L, Category.BAR, 0),
                new Ticket(1L, 1L, 0L, Category.BAR, 0));

        when(ticketRepository.findByUserId(any(Long.class), any(Page.class)))
                .thenReturn(new SearchResult<Ticket>().setContent(testTickets).setPage(Page.of(2, 1)));

        assertEquals(testTickets, ticketService.findByUser(testUser, 2, 1));
        verify(ticketRepository).findByUserId(0L, Page.of(2, 1));
    }

    @Test
    @DisplayName("Должен удалить Ticket из БД и вернуть удалённый объект")
    void shouldDeleteTicketById() {
        Ticket testTicket = new Ticket(0L, 0L, 0L, Category.BAR, 0);

        when(ticketRepository.deleteById(any(Long.class))).thenReturn(testTicket);

        assertEquals(testTicket, ticketService.deleteById(0L));
        verify(ticketRepository).deleteById(0L);
    }
}