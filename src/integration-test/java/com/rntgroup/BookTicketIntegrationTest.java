package com.rntgroup;

import static com.rntgroup.model.Ticket.Category.PREMIUM;
import static com.rntgroup.model.Ticket.Category.STANDARD;
import static java.util.Calendar.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rntgroup.facade.BookingFacade;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class BookTicketIntegrationTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    @DisplayName("Билет успешно забронирован")
    void bookTicketHappyPath() {
        // создание мероприятия
        Event createdEvent = bookingFacade.createEvent(new Event(null, "Хакатон 2022 Java", TestUtil.createDate(2022, OCTOBER, 15)));
        assertEquals(bookingFacade.getEventById(6L), createdEvent);
        assertEquals(1, bookingFacade.getEventsByTitle(createdEvent.getTitle(), 1000, 1).size());

        // создание пользователя
        User createdUser = bookingFacade.createUser(new User(null, "Михаил", "mishamisha@yandex.ru"));
        assertEquals(bookingFacade.getUserById(7L), createdUser);
        assertEquals(1, bookingFacade.getUsersByName("Михаил", 1000, 1).size());

        // бронирование билета
        Ticket bookedTicket = bookingFacade.bookTicket(7L, 6L, 5, PREMIUM);
        assertEquals(8L, bookedTicket.getId());
        assertEquals(1, bookingFacade.getBookedTickets(createdEvent, 1000, 1).size());
    }

    @Test
    @DisplayName("Забронировано несколько билетов, один отменён")
    void cancelBookTicketHappyPath() {
        // создание мероприятия
        Event createdEvent = bookingFacade.createEvent(new Event(null, "Хакатон 2022 Java", TestUtil.createDate(2022, OCTOBER, 15)));
        assertEquals(bookingFacade.getEventById(6L), createdEvent);
        assertEquals(1, bookingFacade.getEventsByTitle(createdEvent.getTitle(), 1000, 1).size());

        // создание пользователя
        User createdUser = bookingFacade.createUser(new User(null, "Михаил", "mishamisha@yandex.ru"));
        assertEquals(bookingFacade.getUserById(7L), createdUser);
        assertEquals(1, bookingFacade.getUsersByName("Михаил", 1000, 1).size());

        // бронирование первого билета
        Ticket firstBookedTicket = bookingFacade.bookTicket(7L, 6L, 5, PREMIUM);
        assertEquals(8L, firstBookedTicket.getId());
        assertEquals(1, bookingFacade.getBookedTickets(createdEvent, 1000, 1).size());
        assertEquals(1, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());

        // бронирование второго билета
        Ticket secondBookedTicket = bookingFacade.bookTicket(7L, 2L, 5, STANDARD);
        assertEquals(9L, secondBookedTicket.getId());
        assertEquals(4, bookingFacade.getBookedTickets(bookingFacade.getEventById(2L), 1000, 1).size());
        assertEquals(2, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());

        // отмена бронирования
        assertTrue(bookingFacade.cancelTicket(secondBookedTicket.getId()));
        assertEquals(3, bookingFacade.getBookedTickets(bookingFacade.getEventById(2L), 1000, 1).size());
        assertEquals(1, bookingFacade.getBookedTickets(createdUser, 1000, 1).size());
    }

    @Test
    @DisplayName("Забронировать билет не получилось. т.к. место уже занято")
    void bookTicketFailPath() {
        // создание пользователя
        User createdUser = bookingFacade.createUser(new User(null, "Михаил", "mishamisha@yandex.ru"));
        assertEquals(bookingFacade.getUserById(7L), createdUser);
        assertEquals(1, bookingFacade.getUsersByName("Михаил", 1000, 1).size());

        // бронирование билета
        var thrown = assertThrows(IllegalStateException.class, () -> bookingFacade.bookTicket(7L, 2L, 10, PREMIUM));
        assertEquals("Ticket with place = 10 already exist", thrown.getMessage());
    }

}
