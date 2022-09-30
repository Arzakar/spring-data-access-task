package com.rntgroup.controller;

import com.rntgroup.dto.BookTicketRequestDto;
import com.rntgroup.facade.BookingFacade;
import com.rntgroup.model.Ticket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {

    BookingFacade bookingFacade;

    @PostMapping
    public Ticket bookTicket(BookTicketRequestDto bookTicketRequest) {
        return bookingFacade.bookTicket(bookTicketRequest.getUserId(),
                bookTicketRequest.getEventId(),
                bookTicketRequest.getPlace(),
                bookTicketRequest.getCategory());
    }

    @GetMapping
    public List<Ticket> getBookedTickets(@RequestParam(name = "userId", required = false) Long userId,
                                         @RequestParam(name = "eventId", required = false) Long eventId,
                                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                         @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {

        if (Objects.nonNull(userId) && Objects.nonNull(eventId)) {
            throw new RuntimeException("Ошибка в запросе");
        }

        if (Objects.isNull(userId) && Objects.isNull(eventId)) {
            throw new RuntimeException("Метод не реализован");
        }

        if (Objects.nonNull(userId)) {
            return bookingFacade.getBookedTickets(bookingFacade.getUserById(userId), pageSize, pageNum);
        }

        return bookingFacade.getBookedTickets(bookingFacade.getEventById(eventId), pageSize, pageNum);
    }

    @DeleteMapping("/{id}")
    public boolean cancelTicket(long ticketId) {
        return bookingFacade.cancelTicket(ticketId);
    }

}
