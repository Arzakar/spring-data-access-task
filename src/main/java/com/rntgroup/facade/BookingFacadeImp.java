package com.rntgroup.facade;

import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.service.EventService;
import com.rntgroup.service.TicketService;
import com.rntgroup.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingFacadeImp implements BookingFacade {

    static Logger LOG = LoggerFactory.getLogger(BookingFacadeImp.class.getSimpleName());

    EventService eventService;
    UserService userService;
    TicketService ticketService;

    @Override
    public Event getEventById(long eventId) {
        LOG.info("Method {}#getEventById was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);
        return eventService.findById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOG.info("Method {}#getEventsByTitle was called with params: title = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), title, pageSize, pageNum);
        return eventService.findByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOG.info("Method {}#getEventsForDay was called with params: day = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), day, pageSize, pageNum);
        return eventService.findByDate(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        LOG.info("Method {}#createEvent was called with param: event = {}", this.getClass().getSimpleName(), event);
        return eventService.create(event);
    }

    @Override
    public Event updateEvent(Event event) {
        LOG.info("Method {}#updateEvent was called with param: event = {}", this.getClass().getSimpleName(), event);
        return eventService.update(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOG.info("Method {}#deleteEvent was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);
        return Objects.nonNull(eventService.deleteById(eventId));
    }

    @Override
    public User getUserById(long userId) {
        LOG.info("Method {}#getUserById was called with param: userId = {}", this.getClass().getSimpleName(), userId);
        return userService.findById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        LOG.info("Method {}#getUserByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return userService.findByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        LOG.info("Method {}#getUsersByName was called with params: name = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), name, pageSize, pageNum);
        return userService.findByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        LOG.info("Method {}#createUser was called with param: user = {}", this.getClass().getSimpleName(), user);
        return userService.create(user);
    }

    @Override
    public User updateUser(User user) {
        LOG.info("Method {}#updateUser was called with param: user = {}", this.getClass().getSimpleName(), user);
        return userService.update(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        LOG.info("Method {}#userId was called with param: userId = {}", this.getClass().getSimpleName(), userId);
        return Objects.nonNull(userService.deleteById(userId));
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        LOG.info("Method {}#bookTicket was called with params: userId = {}, eventId = {}, place = {}, category = {}",
                this.getClass().getSimpleName(), userId, eventId, place, category);
        Ticket ticket = new Ticket(0L, eventId, userId, category, place);
        return ticketService.create(ticket);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        LOG.info("Method {}#getBookedTickets was called with params: user = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), user, pageSize, pageNum);
        return ticketService.findByUser(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        LOG.info("Method {}#getBookedTickets was called with params: event = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), event, pageSize, pageNum);
        return ticketService.findByEvent(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        LOG.info("Method {}#cancelTicket was called with param: ticketId = {}", this.getClass().getSimpleName(), ticketId);
        return Objects.nonNull(ticketService.deleteById(ticketId));
    }
}
