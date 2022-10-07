package com.rntgroup.service;

import com.rntgroup.exception.BadRequestException;
import com.rntgroup.model.Ticket;
import com.rntgroup.repository.TicketRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

        if (ticketRepository.findByEventIdAndPlace(ticket.getEvent().getId(), ticket.getPlace()).isPresent()) {
            var error = new BadRequestException(String.format("Ticket with place = %d already exist", ticket.getPlace()));
            log.error("Method {}#create returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
            throw error;
        }

        return ticketRepository.save(ticket);
    }

    public List<Ticket> findByUserId(UUID userId, int pageSize, int pageNum) {
        log.debug("Method {}#findByUser was called with params: userId = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), userId, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "event");
        return ticketRepository.findByUserId(userId, pageRequest).getContent();
    }

    public List<Ticket> findByEventId(UUID eventId, int pageSize, int pageNum) {
        log.debug("Method {}#findByEvent was called with params: eventId = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), eventId, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "user");
        return ticketRepository.findByEventId(eventId, pageRequest).getContent();
    }

    public boolean deleteById(UUID id) {
        log.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        ticketRepository.deleteById(id);
        return !ticketRepository.existsById(id);
    }
}
