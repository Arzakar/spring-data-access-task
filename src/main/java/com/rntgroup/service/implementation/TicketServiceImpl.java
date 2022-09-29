package com.rntgroup.service.implementation;

import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.repository.TicketRepository;
import com.rntgroup.repository.util.Page;
import com.rntgroup.service.TicketService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketServiceImpl implements TicketService {

    static final Logger LOG = LoggerFactory.getLogger(TicketServiceImpl.class.getSimpleName());

    TicketRepository ticketRepository;

    @Override
    public Ticket create(Ticket ticket) {
        LOG.debug("Method {}#create was called with param: ticket = {}", this.getClass().getSimpleName(), ticket);
        if (ticketRepository.existByPlace(ticket)) {
            var error = new IllegalStateException(String.format("Ticket with place = %d already exist", ticket.getPlace()));
            LOG.error("Method {}#create returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
            throw error;
        }

        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findByUser(User user, int pageSize, int pageNum) {
        LOG.debug("Method {}#findByUser was called with params: user = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), user, pageSize, pageNum);
        return ticketRepository.findByUserId(user.getId(), Page.of(pageSize, pageNum))
                .getContent();
    }

    @Override
    public List<Ticket> findByEvent(Event event, int pageSize, int pageNum) {
        LOG.debug("Method {}#findByEvent was called with params: event = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), event, pageSize, pageNum);
        return ticketRepository.findByEventId(event.getId(), Page.of(pageSize, pageNum))
                .getContent();
    }

    @Override
    public Ticket deleteById(long id) {
        LOG.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return ticketRepository.deleteById(id);
    }
}
