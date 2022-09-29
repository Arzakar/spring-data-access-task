package com.rntgroup.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestUtil;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;

import com.rntgroup.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TicketDatabaseTest {

    @Mock
    private UserDatabase userDatabase;

    @Mock
    private EventDatabase eventDatabase;

    @InjectMocks
    private TicketDatabase ticketDatabase;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<Long, Ticket> testTickets = objectMapper.readValue(
                TestUtil.readResourceAsString("db/init-ticket-data.json"),
                new TypeReference<>() {}
        );
        ticketDatabase.setData(testTickets);
    }

    @ParameterizedTest(name = "[{index}] Event id = {0}, tickets with this event id = {1}")
    @MethodSource("getEventIds")
    @DisplayName("Должен вернуть все Ticket из БД с конкретным eventId")
    void shouldReturnTicketsByEventId(long eventId, int expectedListSize) {
        lenient().when(userDatabase.selectById(any(Long.class))).thenReturn(new User(0L, "TestUser", "test_email@ya.ru"));
        List<Ticket> actualResult = ticketDatabase.selectByEventId(eventId);

        assertEquals(expectedListSize, actualResult.size());
        assertTrue(actualResult.stream().allMatch(event -> event.getEventId() == eventId));
    }

    private static Stream<Arguments> getEventIds() {
        return Stream.of(
                Arguments.of(2, 3),
                Arguments.of(4, 2),
                Arguments.of(6, 0)
        );
    }

    @ParameterizedTest(name = "[{index}] User id = {0}, tickets with this userId = {1}")
    @MethodSource("getUserIds")
    @DisplayName("Должен вернуть все Ticket из БД с конкретным userId")
    void shouldReturnTicketsByUserId(long userId, int expectedListSize) {
        lenient().when(eventDatabase.selectById(any(Long.class))).thenReturn(new Event(0L, "TestEvent", new Date()));

        List<Ticket> actualResult = ticketDatabase.selectByUserId(userId);

        assertEquals(expectedListSize, actualResult.size());
        assertTrue(actualResult.stream().allMatch(event -> event.getUserId() == userId));
    }

    private static Stream<Arguments> getUserIds() {
        return Stream.of(
                Arguments.of(3, 2),
                Arguments.of(1, 1),
                Arguments.of(2, 0)
        );
    }

    @ParameterizedTest(name = "[{index}] Event id = {0}, ticket place = {1}, tickets with this eventId and place  = {2}")
    @MethodSource("getEventIdsAndPlaces")
    @DisplayName("Должен вернуть Ticket из БД с конкретным eventId и place или null")
    void shouldReturnTicketByEventIdAndPlace(long eventId, int place, Ticket expectedResult) {
        Ticket actualResult = ticketDatabase.selectByEventIdAndPlace(eventId, place);

        assertEquals(expectedResult, actualResult);
    }

    private static Stream<Arguments> getEventIdsAndPlaces() {
        return Stream.of(
                Arguments.of(2, 1, new Ticket(0L, 2L, 1L, Ticket.Category.BAR, 1)),
                Arguments.of(2, 5, null)
        );
    }
}