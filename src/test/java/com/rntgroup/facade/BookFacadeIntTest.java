package com.rntgroup.facade;

import static com.rntgroup.enumerate.Category.PREMIUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rntgroup.TestDataUtil;
import com.rntgroup.dto.EventDto;
import com.rntgroup.dto.TicketDto;
import com.rntgroup.dto.UserDto;
import com.rntgroup.enumerate.Category;
import com.rntgroup.exception.ValidationException;
import com.rntgroup.mapper.EventMapper;
import com.rntgroup.mapper.TicketMapper;
import com.rntgroup.mapper.UserMapper;

import com.rntgroup.repository.EventRepository;
import com.rntgroup.repository.TicketRepository;
import com.rntgroup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookFacadeIntTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    @DisplayName("Билет успешно забронирован")
    void bookTicketHappyPath() {
        // создание мероприятия
        EventDto eventDto = bookingFacade.createEvent(TestDataUtil.getRandomEventDto());
        assertEquals(eventMapper.toModel(eventDto), eventRepository.findById(eventDto.getId()).orElseThrow());
        assertEquals(1, eventRepository.findAll().size());

        // создание пользователя
        UserDto userDto = bookingFacade.createUser(TestDataUtil.getRandomUserDto());
        assertEquals(userMapper.toModel(userDto), userRepository.findById(userDto.getId()).orElseThrow());
        assertEquals(1, userRepository.findAll().size());

        // бронирование билета
        TicketDto bookedTicketDto = bookingFacade.bookTicket(userDto.getId(), eventDto.getId(), 5, Category.BAR);
        assertEquals(ticketMapper.toModel(bookedTicketDto), ticketRepository.findById(bookedTicketDto.getId()).orElseThrow());
        assertEquals(1, ticketRepository.findAll().size());
    }

    @Test
    @DisplayName("Забронировано несколько билетов, один отменён")
    void cancelTicketHappyPath() {
        // создание мероприятия
        EventDto eventDto = bookingFacade.createEvent(TestDataUtil.getRandomEventDto());
        assertEquals(eventMapper.toModel(eventDto), eventRepository.findById(eventDto.getId()).orElseThrow());
        assertEquals(1, eventRepository.findAll().size());

        // создание пользователя
        UserDto userDto = bookingFacade.createUser(TestDataUtil.getRandomUserDto());
        assertEquals(userMapper.toModel(userDto), userRepository.findById(userDto.getId()).orElseThrow());
        assertEquals(1, userRepository.findAll().size());

        // бронирование первого билета
        TicketDto firstBookedTicketDto = bookingFacade.bookTicket(userDto.getId(), eventDto.getId(), 5, Category.BAR);
        assertEquals(ticketMapper.toModel(firstBookedTicketDto), ticketRepository.findById(firstBookedTicketDto.getId()).orElseThrow());
        assertEquals(1, ticketRepository.findAll().size());

        // бронирование второго билета
        TicketDto secondBookedTicketDto = bookingFacade.bookTicket(userDto.getId(), eventDto.getId(), 6, Category.BAR);
        assertEquals(ticketMapper.toModel(secondBookedTicketDto), ticketRepository.findById(secondBookedTicketDto.getId()).orElseThrow());
        assertEquals(2, ticketRepository.findAll().size());

        // отмена бронирования второго билета
        assertTrue(bookingFacade.cancelTicket(secondBookedTicketDto.getId()));
        assertEquals(1, ticketRepository.findAll().size());
        assertNotNull(ticketRepository.findById(firstBookedTicketDto.getId()));
    }

    @Test
    @DisplayName("Забронировать билет не получилось. т.к. место уже занято")
    void bookTicketFailPath() {
        // создание мероприятия
        EventDto eventDto = bookingFacade.createEvent(TestDataUtil.getRandomEventDto());
        assertEquals(eventMapper.toModel(eventDto), eventRepository.findById(eventDto.getId()).orElseThrow());
        assertEquals(1, eventRepository.findAll().size());

        // создание пользователя
        UserDto userDto = bookingFacade.createUser(TestDataUtil.getRandomUserDto());
        assertEquals(userMapper.toModel(userDto), userRepository.findById(userDto.getId()).orElseThrow());
        assertEquals(1, userRepository.findAll().size());

        // бронирование первого билета
        TicketDto firstBookedTicketDto = bookingFacade.bookTicket(userDto.getId(), eventDto.getId(), 5, Category.BAR);
        assertEquals(ticketMapper.toModel(firstBookedTicketDto), ticketRepository.findById(firstBookedTicketDto.getId()).orElseThrow());
        assertEquals(1, ticketRepository.findAll().size());

        // бронирование второго билета
        UUID eventId = eventDto.getId();
        UUID userId = userDto.getId();
        var thrown = assertThrows(ValidationException.class, () ->
                bookingFacade.bookTicket(userId, eventId, 5, PREMIUM));
        assertEquals("Ticket with place = 5 already exist", thrown.getMessage());
    }

}
