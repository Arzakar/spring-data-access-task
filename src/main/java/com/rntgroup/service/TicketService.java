package com.rntgroup.service;

import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.repository.TicketRepository;
import com.rntgroup.repository.util.Page;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketService {

    TicketRepository ticketRepository;

    public Ticket create(Ticket ticket) {
        log.debug("Method {}#create was called with param: ticket = {}", this.getClass().getSimpleName(), ticket);
        if (ticketRepository.existByPlace(ticket)) {
            var error = new IllegalStateException(String.format("Ticket with place = %d already exist", ticket.getPlace()));
            log.error("Method {}#create returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
            throw error;
        }

        return ticketRepository.save(ticket);
    }

    public List<Ticket> findByUser(User user, int pageSize, int pageNum) {
        log.debug("Method {}#findByUser was called with params: user = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), user, pageSize, pageNum);
        return ticketRepository.findByUserId(user.getId(), Page.of(pageSize, pageNum))
                .getContent();
    }

    public List<Ticket> findByEvent(Event event, int pageSize, int pageNum) {
        log.debug("Method {}#findByEvent was called with params: event = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), event, pageSize, pageNum);
        return ticketRepository.findByEventId(event.getId(), Page.of(pageSize, pageNum))
                .getContent();
    }

    public Ticket deleteById(long id) {
        log.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return ticketRepository.deleteById(id);
    }
}
