package com.rntgroup.service;

import com.rntgroup.exception.NotFoundException;
import com.rntgroup.exception.ValidationException;
import com.rntgroup.model.Ticket;
import com.rntgroup.repository.TicketRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketService {

    TicketRepository ticketRepository;

    public Ticket findById(UUID id) {
        return ticketRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Ticket with id = %s not found", id)));
    }

    public Ticket create(Ticket ticket) {
        log.debug("Method {}#create was called with param: ticket = {}", this.getClass().getSimpleName(), ticket);

        if (ticketRepository.findByEventIdAndPlace(ticket.getEvent().getId(), ticket.getPlace()).isPresent()) {
            throw new ValidationException(String.format("Ticket with place = %d already exist", ticket.getPlace()));
        }

        return ticketRepository.save(ticket);
    }

    public List<Ticket> findByUserId(UUID userId, Pageable pageable) {
        log.debug("Method {}#findByUser was called with params: userId = {}, pageable = {}",
                this.getClass().getSimpleName(), userId, pageable);

        return ticketRepository.findByUserId(userId, pageable).getContent();
    }

    public List<Ticket> findByEventId(UUID eventId, Pageable pageable) {
        log.debug("Method {}#findByEvent was called with params: eventId = {}, pageable = {}",
                this.getClass().getSimpleName(), eventId, pageable);

        return ticketRepository.findByEventId(eventId, pageable).getContent();
    }

    public boolean deleteById(UUID id) {
        log.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);

        ticketRepository.deleteById(id);
        return !ticketRepository.existsById(id);
    }
}
