package com.rntgroup.facade;

import static com.rntgroup.enumerate.Category.PREMIUM;
import static com.rntgroup.enumerate.Category.STANDARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rntgroup.TestDataUtil;
import com.rntgroup.exception.BadRequestException;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;

import com.rntgroup.repository.EventRepository;
import com.rntgroup.repository.TicketRepository;
import com.rntgroup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookFacadeIntTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Билет успешно забронирован")
    void bookTicketHappyPath() {
        int pageSize = 10;
        int pageNum = 1;

        // создание мероприятия
        Event event = bookingFacade.createEvent(TestDataUtil.getRandomEvent());
        assertEquals(1, bookingFacade.getEventsByTitle(event.getTitle(), pageSize, pageNum).size());
        assertEquals(event, eventRepository.findById(event.getId()).orElseThrow());

        // создание пользователя
        User user = bookingFacade.createUser(TestDataUtil.getRandomUser());
        assertEquals(1, bookingFacade.getUsersByName(user.getName(), pageSize, pageNum).size());
        assertEquals(user, userRepository.findById(user.getId()).orElseThrow());

        // бронирование билета
        Ticket bookedTicket = bookingFacade.bookTicket(user.getId(), event.getId(), 5, PREMIUM);
        assertEquals(1, bookingFacade.getBookedTickets(event, pageSize, pageNum).size());
        assertEquals(bookedTicket, ticketRepository.findById(bookedTicket.getId()).orElseThrow());
    }

    @Test
    @DisplayName("Забронировано несколько билетов, один отменён")
    void cancelTicketHappyPath() {
        int pageSize = 10;
        int pageNum = 1;

        // создание мероприятия
        Event event = bookingFacade.createEvent(TestDataUtil.getRandomEvent());
        assertEquals(1, bookingFacade.getEventsByTitle(event.getTitle(), pageSize, pageNum).size());
        assertEquals(event, eventRepository.findById(event.getId()).orElseThrow());

        // создание пользователя
        User user = bookingFacade.createUser(TestDataUtil.getRandomUser());
        assertEquals(1, bookingFacade.getUsersByName(user.getName(), pageSize, pageNum).size());
        assertEquals(user, userRepository.findById(user.getId()).orElseThrow());

        // бронирование первого билета
        Ticket firstBookedTicket = bookingFacade.bookTicket(user.getId(), event.getId(), 5, PREMIUM);
        assertEquals(1, bookingFacade.getBookedTickets(event, pageSize, pageNum).size());
        assertEquals(firstBookedTicket, ticketRepository.findById(firstBookedTicket.getId()).orElseThrow());

        // бронирование второго билета
        Ticket secondBookedTicket = bookingFacade.bookTicket(user.getId(), event.getId(), 6, STANDARD);
        assertEquals(2, bookingFacade.getBookedTickets(event, pageSize, pageNum).size());
        assertEquals(secondBookedTicket, ticketRepository.findById(secondBookedTicket.getId()).orElseThrow());

        // отмена бронирования
        assertTrue(bookingFacade.cancelTicket(secondBookedTicket.getId()));
        assertEquals(1, bookingFacade.getBookedTickets(event, pageSize, pageNum).size());
        assertEquals(1, bookingFacade.getBookedTickets(user, pageSize, pageNum).size());
    }

    @Test
    @DisplayName("Забронировать билет не получилось. т.к. место уже занято")
    void bookTicketFailPath() {
        int pageSize = 10;
        int pageNum = 1;

        // создание мероприятия
        Event event = bookingFacade.createEvent(TestDataUtil.getRandomEvent());
        assertEquals(1, bookingFacade.getEventsByTitle(event.getTitle(), pageSize, pageNum).size());
        assertEquals(event, eventRepository.findById(event.getId()).orElseThrow());

        // создание пользователя
        User user = bookingFacade.createUser(TestDataUtil.getRandomUser());
        assertEquals(1, bookingFacade.getUsersByName(user.getName(), pageSize, pageNum).size());
        assertEquals(user, userRepository.findById(user.getId()).orElseThrow());

        // бронирование первого билета
        Ticket firstBookedTicket = bookingFacade.bookTicket(user.getId(), event.getId(), 5, PREMIUM);
        assertEquals(1, bookingFacade.getBookedTickets(event, pageSize, pageNum).size());
        assertEquals(firstBookedTicket, ticketRepository.findById(firstBookedTicket.getId()).orElseThrow());

        // бронирование второго билета
        UUID userId = user.getId();
        UUID eventId = event.getId();
        var thrown = assertThrows(BadRequestException.class, () ->
                bookingFacade.bookTicket(userId, eventId, 5, PREMIUM));
        assertEquals("Ticket with place = 5 already exist", thrown.getMessage());
    }

}
