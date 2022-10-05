package com.rntgroup.facade;

import com.rntgroup.dto.xml.EventXmlDtoList;
import com.rntgroup.dto.xml.TicketXmlDtoList;
import com.rntgroup.dto.xml.UserXmlDtoList;
import com.rntgroup.enumerate.Category;
import com.rntgroup.mapper.EventMapper;
import com.rntgroup.mapper.TicketMapper;
import com.rntgroup.mapper.UserMapper;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.service.EventService;
import com.rntgroup.service.TicketService;
import com.rntgroup.service.UserService;

import com.rntgroup.util.FileReader;
import com.rntgroup.util.MarshallerWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingFacadeImp implements BookingFacade {

    EventService eventService;
    UserService userService;
    TicketService ticketService;

    EventMapper eventMapper;
    UserMapper userMapper;
    TicketMapper ticketMapper;
    MarshallerWrapper marshallerWrapper;

    @Override
    public Event getEventById(UUID eventId) {
        log.info("Method {}#getEventById was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);
        return eventService.findById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        log.info("Method {}#getEventsByTitle was called with params: title = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), title, pageSize, pageNum);
        return eventService.findByTitle(title, pageSize, pageNumOffset(pageNum));
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        log.info("Method {}#getEventsForDay was called with params: day = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), day, pageSize, pageNum);
        return eventService.findByDate(day, pageSize, pageNumOffset(pageNum));
    }

    @Override
    public Event createEvent(Event event) {
        log.info("Method {}#createEvent was called with param: event = {}", this.getClass().getSimpleName(), event);
        return eventService.create(event);
    }

    @Override
    public Event updateEvent(Event event) {
        log.info("Method {}#updateEvent was called with param: event = {}", this.getClass().getSimpleName(), event);
        return eventService.update(event);
    }

    @Override
    public boolean deleteEvent(UUID eventId) {
        log.info("Method {}#deleteEvent was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);
        return eventService.deleteById(eventId);
    }

    @Override
    public User getUserById(UUID userId) {
        log.info("Method {}#getUserById was called with param: userId = {}", this.getClass().getSimpleName(), userId);
        return userService.findById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Method {}#getUserByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return userService.findByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        log.info("Method {}#getUsersByName was called with params: name = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), name, pageSize, pageNum);
        return userService.findByName(name, pageSize, pageNumOffset(pageNum));
    }

    @Override
    public User createUser(User user) {
        log.info("Method {}#createUser was called with param: user = {}", this.getClass().getSimpleName(), user);
        return userService.create(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Method {}#updateUser was called with param: user = {}", this.getClass().getSimpleName(), user);
        return userService.update(user);
    }

    @Override
    public boolean deleteUser(UUID userId) {
        log.info("Method {}#userId was called with param: userId = {}", this.getClass().getSimpleName(), userId);
        return userService.deleteById(userId);
    }

    @Override
    public Ticket bookTicket(UUID userId, UUID eventId, int place, Category category) {
        log.info("Method {}#bookTicket was called with params: userId = {}, eventId = {}, place = {}, category = {}",
                this.getClass().getSimpleName(), userId, eventId, place, category);
        Event event = getEventById(eventId);
        User user = getUserById(userId);
        Ticket ticket = new Ticket()
                .setEvent(event)
                .setUser(user)
                .setCategory(category)
                .setPlace(place);
        return ticketService.create(ticket);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        log.info("Method {}#getBookedTickets was called with params: user = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), user, pageSize, pageNum);
        return ticketService.findByUserId(user.getId(), pageSize, pageNumOffset(pageNum));
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        log.info("Method {}#getBookedTickets was called with params: event = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), event, pageSize, pageNum);
        return ticketService.findByEventId(event.getId(), pageSize, pageNumOffset(pageNum));
    }

    @Override
    public boolean cancelTicket(UUID ticketId) {
        log.info("Method {}#cancelTicket was called with param: ticketId = {}", this.getClass().getSimpleName(), ticketId);
        return ticketService.deleteById(ticketId);
    }

    public void preloadTickets() throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;

        InputStream usersInputStream = new ByteArrayInputStream(
                FileReader.readResourceAsString("init-data/users.xml").getBytes(utf8)
        );
        UserXmlDtoList users = marshallerWrapper.unmarshall(usersInputStream, UserXmlDtoList.class);
        users.getUsers().stream()
                .map(userMapper::toModel)
                .forEach(userService::create);

        InputStream eventsInputStream = new ByteArrayInputStream(
                FileReader.readResourceAsString("init-data/events.xml").getBytes(utf8)
        );
        EventXmlDtoList events = marshallerWrapper.unmarshall(eventsInputStream, EventXmlDtoList.class);
        events.getEvents().stream()
                .map(eventMapper::toModel)
                .forEach(eventService::create);

        InputStream ticketsInputStream = new ByteArrayInputStream(
                FileReader.readResourceAsString("init-data/tickets.xml").getBytes(utf8)
        );
        TicketXmlDtoList tickets = marshallerWrapper.unmarshall(ticketsInputStream, TicketXmlDtoList.class);
        tickets.getTickets().stream()
                .map(ticketMapper::toModel)
                .forEach(ticketService::create);
    }

    private int pageNumOffset(int pageNum) {
        return pageNum > 0 ? pageNum - 1 : 0;
    }
}
