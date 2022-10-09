package com.rntgroup.facade;

import com.rntgroup.dto.EventDto;
import com.rntgroup.dto.TicketDto;
import com.rntgroup.dto.UserAccountDto;
import com.rntgroup.dto.UserDto;
import com.rntgroup.dto.EventDtoList;
import com.rntgroup.dto.TicketDtoList;
import com.rntgroup.dto.UserDtoList;
import com.rntgroup.enumerate.Category;
import com.rntgroup.mapper.EventMapper;
import com.rntgroup.mapper.TicketMapper;
import com.rntgroup.mapper.UserAccountMapper;
import com.rntgroup.mapper.UserMapper;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import com.rntgroup.model.UserAccount;
import com.rntgroup.service.EventService;
import com.rntgroup.service.TicketService;
import com.rntgroup.service.UserAccountService;
import com.rntgroup.service.UserService;
import com.rntgroup.util.FileReader;
import com.rntgroup.util.MarshallerWrapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingFacade {

    EventService eventService;
    TicketService ticketService;
    UserService userService;
    UserAccountService userAccountService;

    EventMapper eventMapper;
    TicketMapper ticketMapper;
    UserMapper userMapper;
    UserAccountMapper userAccountMapper;

    MarshallerWrapper marshallerWrapper;

    //@Override
    public EventDto getEventById(UUID eventId) {
        log.info("Method {}#getEventById was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);

        Event event = eventService.findById(eventId);
        return eventMapper.toDto(event);
    }

    //@Override
    public List<EventDto> getEventsByTitle(String title, int pageNum, int pageSize, Sort.Direction direction, String sortParam) {
        log.info("Method {}#getEventsByTitle was called with params: title = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), title, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNumOffset(pageNum), pageSize, direction, sortParam);
        List<Event> events = eventService.findByTitle(title, pageRequest);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    //@Override
    public List<EventDto> getEventsForDay(LocalDate day, int pageNum, int pageSize, Sort.Direction direction, String sortParam) {
        log.info("Method {}#getEventsForDay was called with params: day = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), day, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNumOffset(pageNum), pageSize, direction, sortParam);
        List<Event> events = eventService.findByDate(day, pageRequest);
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    //@Override
    public EventDto createEvent(EventDto event) {
        log.info("Method {}#createEvent was called with param: event = {}", this.getClass().getSimpleName(), event);

        Event createdEvent = eventService.create(eventMapper.toModel(event));
        return eventMapper.toDto(createdEvent);
    }

    //@Override
    public EventDto updateEvent(EventDto event) {
        log.info("Method {}#updateEvent was called with param: event = {}", this.getClass().getSimpleName(), event);

        Event updatedEvent = eventService.update(eventMapper.toModel(event));
        return eventMapper.toDto(updatedEvent);
    }

    //@Override
    public boolean deleteEvent(UUID eventId) {
        log.info("Method {}#deleteEvent was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);

        return eventService.deleteById(eventId);
    }

    //@Override
    public UserDto getUserById(UUID userId) {
        log.info("Method {}#getUserById was called with param: userId = {}", this.getClass().getSimpleName(), userId);

        User user = userService.findById(userId);
        return userMapper.toDto(user);
    }

    //@Override
    public UserDto getUserByEmail(String email) {
        log.info("Method {}#getUserByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);

        User user = userService.findByEmail(email);
        return userMapper.toDto(user);
    }

    //@Override
    public List<UserDto> getUsersByName(String name, int pageNum, int pageSize, Sort.Direction direction, String sortParam) {
        log.info("Method {}#getUsersByName was called with params: name = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), name, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNumOffset(pageNum), pageSize, direction, sortParam);
        List<User> users = userService.findByName(name, pageRequest);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    //@Override
    public UserDto createUser(UserDto user) {
        log.info("Method {}#createUser was called with param: user = {}", this.getClass().getSimpleName(), user);

        User createdUser = userService.create(userMapper.toModel(user));
        return userMapper.toDto(createdUser);
    }

    //@Override
    public UserDto updateUser(UserDto user) {
        log.info("Method {}#updateUser was called with param: user = {}", this.getClass().getSimpleName(), user);

        User updatedUser = userService.update(userMapper.toModel(user));
        return userMapper.toDto(updatedUser);
    }

    //@Override
    public boolean deleteUser(UUID userId) {
        log.info("Method {}#userId was called with param: userId = {}", this.getClass().getSimpleName(), userId);

        return userService.deleteById(userId);
    }

    //@Override
    public TicketDto bookTicket(UUID userId, UUID eventId, int place, Category category) {
        log.info("Method {}#bookTicket was called with params: userId = {}, eventId = {}, place = {}, category = {}",
                this.getClass().getSimpleName(), userId, eventId, place, category);

        Event event = eventService.findById(eventId);
        User user = userService.findById(userId);
        Ticket bookedTicket = ticketService.create(
                new Ticket().setEvent(event)
                        .setUser(user)
                        .setCategory(category)
                        .setPlace(place)
        );

        return ticketMapper.toDto(bookedTicket);
    }

    //@Override
    public List<TicketDto> getBookedTickets(UserDto userDto, int pageNum, int pageSize, Sort.Direction direction, String sortParam) {
        log.info("Method {}#getBookedTickets was called with params: userId = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), userDto.getId(), pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNumOffset(pageNum), pageSize, direction, sortParam);
        List<Ticket> tickets = ticketService.findByUserId(userDto.getId(), pageRequest);
        return tickets.stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    //@Override
    public List<TicketDto> getBookedTickets(EventDto eventDto, int pageNum, int pageSize, Sort.Direction direction, String sortParam) {
        log.info("Method {}#getBookedTickets was called with params: eventId = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), eventDto.getId(), pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNumOffset(pageNum), pageSize, direction, sortParam);
        List<Ticket> tickets = ticketService.findByEventId(eventDto.getId(), pageRequest);
        return tickets.stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    //@Override
    public boolean cancelTicket(UUID ticketId) {
        log.info("Method {}#cancelTicket was called with param: ticketId = {}", this.getClass().getSimpleName(), ticketId);

        return ticketService.deleteById(ticketId);
    }

    //@Override
    public UserAccountDto replenishAccount(UUID userId, BigDecimal amount) {
        UserAccount userAccount = userAccountService.replenish(userId, amount);
        return userAccountMapper.toDto(userAccount);
    }

    public void preloadTickets() throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;

        InputStream usersInputStream = new ByteArrayInputStream(
                FileReader.readResourceAsString("init-data/users.xml").getBytes(utf8)
        );
        UserDtoList users = marshallerWrapper.unmarshall(usersInputStream, UserDtoList.class);
        users.getUsers().stream()
                .map(userMapper::toModel)
                .forEach(userService::create);

        InputStream eventsInputStream = new ByteArrayInputStream(
                FileReader.readResourceAsString("init-data/events.xml").getBytes(utf8)
        );
        EventDtoList events = marshallerWrapper.unmarshall(eventsInputStream, EventDtoList.class);
        events.getEvents().stream()
                .map(eventMapper::toModel)
                .forEach(eventService::create);

        InputStream ticketsInputStream = new ByteArrayInputStream(
                FileReader.readResourceAsString("init-data/tickets.xml").getBytes(utf8)
        );
        TicketDtoList tickets = marshallerWrapper.unmarshall(ticketsInputStream, TicketDtoList.class);
        tickets.getTickets().stream()
                .map(ticketMapper::toModel)
                .forEach(ticketService::create);
    }

    private int pageNumOffset(int pageNum) {
        return pageNum > 0 ? pageNum - 1 : 0;
    }
}
