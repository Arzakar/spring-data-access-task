package com.rntgroup.facade;

import static com.rntgroup.enumerate.Category.PREMIUM;
import static com.rntgroup.enumerate.Category.STANDARD;
import static java.util.Calendar.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rntgroup.TestUtil;
import com.rntgroup.db.EventDatabase;
import com.rntgroup.db.TicketDatabase;
import com.rntgroup.db.UserDatabase;
import com.rntgroup.exception.BadRequestException;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookFacadeIntTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private EventDatabase eventDatabase;

    @Autowired
    private TicketDatabase ticketDatabase;

    @Autowired
    private UserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        eventDatabase.getData().clear();
        ticketDatabase.getData().clear();
        userDatabase.getData().clear();
    }

    @Test
    @DisplayName("Билет успешно забронирован")
    void bookTicketHappyPath() {
        // создание мероприятия
        Event createdEvent = bookingFacade.createEvent(new Event(null, "Хакатон 2022 Java", TestUtil.createDate(2022, OCTOBER, 15)));
        assertEquals(bookingFacade.getEventById(0L), createdEvent);
        assertEquals(1, bookingFacade.getEventsByTitle(createdEvent.getTitle(), 1000, 1).size());

        // создание пользователя
        User createdUser = bookingFacade.createUser(new User(null, "Михаил", "mishamisha@yandex.ru"));
        assertEquals(bookingFacade.getUserById(0L), createdUser);
        assertEquals(1, bookingFacade.getUsersByName("Михаил", 1000, 1).size());

        // бронирование билета
        Ticket bookedTicket = bookingFacade.bookTicket(0L, 0L, 5, PREMIUM);
        assertEquals(0L, bookedTicket.getId());
        assertEquals(1, bookingFacade.getBookedTickets(createdEvent, 1000, 1).size());
    }

    @Test
    @DisplayName("Забронировано несколько билетов, один отменён")
    void cancelTicketHappyPath() {
        // создание мероприятия
        Event createdEvent = bookingFacade.createEvent(new Event(null, "Хакатон 2022 Java", TestUtil.createDate(2022, OCTOBER, 15)));
        assertEquals(bookingFacade.getEventById(0L), createdEvent);
        assertEquals(1, bookingFacade.getEventsByTitle(createdEvent.getTitle(), 1000, 1).size());

        // создание пользователя
        User createdUser = bookingFacade.createUser(new User(null, "Михаил", "mishamisha@yandex.ru"));
        assertEquals(bookingFacade.getUserById(0L), createdUser);
        assertEquals(1, bookingFacade.getUsersByName("Михаил", 1000, 1).size());

        // бронирование первого билета
        Ticket firstBookedTicket = bookingFacade.bookTicket(0L, 0L, 5, PREMIUM);
        assertEquals(0L, firstBookedTicket.getId());
        assertEquals(1, bookingFacade.getBookedTickets(createdEvent, 1000, 1).size());
        assertEquals(1, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());

        // бронирование второго билета
        Ticket secondBookedTicket = bookingFacade.bookTicket(0L, 0L, 6, STANDARD);
        assertEquals(1L, secondBookedTicket.getId());
        assertEquals(2, bookingFacade.getBookedTickets(bookingFacade.getEventById(0L), 1000, 1).size());
        assertEquals(2, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());

        // отмена бронирования
        assertTrue(bookingFacade.cancelTicket(secondBookedTicket.getId()));
        assertEquals(1, bookingFacade.getBookedTickets(bookingFacade.getEventById(0L), 1000, 1).size());
        assertEquals(1, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());
    }

    @Test
    @DisplayName("Забронировать билет не получилось. т.к. место уже занято")
    void bookTicketFailPath() {
        // создание мероприятия
        Event createdEvent = bookingFacade.createEvent(new Event(null, "Хакатон 2022 Java", TestUtil.createDate(2022, OCTOBER, 15)));
        assertEquals(bookingFacade.getEventById(0L), createdEvent);
        assertEquals(1, bookingFacade.getEventsByTitle(createdEvent.getTitle(), 1000, 1).size());

        // создание пользователя
        User createdUser = bookingFacade.createUser(new User(null, "Михаил", "mishamisha@yandex.ru"));
        assertEquals(bookingFacade.getUserById(0L), createdUser);
        assertEquals(1, bookingFacade.getUsersByName("Михаил", 1000, 1).size());

        // бронирование первого билета
        Ticket firstBookedTicket = bookingFacade.bookTicket(0L, 0L, 5, PREMIUM);
        assertEquals(0L, firstBookedTicket.getId());
        assertEquals(1, bookingFacade.getBookedTickets(createdEvent, 1000, 1).size());
        assertEquals(1, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());

        // бронирование второго билета
        var thrown = assertThrows(BadRequestException.class, () -> bookingFacade.bookTicket(0L, 0L, 5, PREMIUM));
        assertEquals("Ticket with place = 5 already exist", thrown.getMessage());
    }

}
