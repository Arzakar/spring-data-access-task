package com.rntgroup.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestUtil;
import com.rntgroup.db.TicketDatabase;
import com.rntgroup.model.Ticket;
import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TicketRepositoryTest {

    @Mock
    private TicketDatabase ticketDatabase;

    @InjectMocks
    private TicketRepository ticketRepository;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @ParameterizedTest(name = "[{index}] Page = {1}, expected search result = {2}")
    @MethodSource("getTicketsByEventId")
    @DisplayName("Должен вернуть определённую страницу с результатом поиска по eventId")
    void shouldReturnSearchResultByEventId(List<Ticket> responseTicketsByEventId, Page page, SearchResult<Ticket> expectedResult) {
        when(ticketDatabase.selectByEventId(any(Long.class))).thenReturn(responseTicketsByEventId);

        SearchResult<Ticket> actualResult = ticketRepository.findByEventId(0L, page);

        assertEquals(expectedResult, actualResult);
    }

    @SneakyThrows
    private static Stream<Arguments> getTicketsByEventId() {
        List<Ticket> responseTicketsByEventId = OBJECT_MAPPER.readValue(
                TestUtil.readResourceAsString("repository/response/tickets-by-event-id.json"),
                new TypeReference<>() {
                }
        );

        return Stream.of(
                Arguments.of(responseTicketsByEventId, Page.of(3, 1), SearchResult.pack(responseTicketsByEventId, Page.of(3, 1))),
                Arguments.of(responseTicketsByEventId, Page.of(2, 3), SearchResult.pack(responseTicketsByEventId, Page.of(2, 3))),
                Arguments.of(responseTicketsByEventId, Page.of(4, 2), SearchResult.pack(responseTicketsByEventId, Page.of(4, 2)))
        );
    }

    @ParameterizedTest(name = "[{index}] Page = {1}, expected search result = {2}")
    @MethodSource("getTicketsByUserId")
    @DisplayName("Должен вернуть определённую страницу с результатом поиска по userId")
    void shouldReturnSearchResultByUserId(List<Ticket> responseTicketsByUserId, Page page, SearchResult<Ticket> expectedResult) {
        when(ticketDatabase.selectByUserId(any(Long.class))).thenReturn(responseTicketsByUserId);

        SearchResult<Ticket> actualResult = ticketRepository.findByUserId(0L, page);

        assertEquals(expectedResult, actualResult);
    }

    @SneakyThrows
    private static Stream<Arguments> getTicketsByUserId() {
        List<Ticket> responseTicketsByEventId = OBJECT_MAPPER.readValue(
                TestUtil.readResourceAsString("repository/response/tickets-by-user-id.json"),
                new TypeReference<>() {
                }
        );

        return Stream.of(
                Arguments.of(responseTicketsByEventId, Page.of(3, 1), SearchResult.pack(responseTicketsByEventId, Page.of(3, 1))),
                Arguments.of(responseTicketsByEventId, Page.of(2, 3), SearchResult.pack(responseTicketsByEventId, Page.of(2, 3))),
                Arguments.of(responseTicketsByEventId, Page.of(4, 2), SearchResult.pack(responseTicketsByEventId, Page.of(4, 2)))
        );
    }

    @Test
    @DisplayName("Должен проверить занято ли конкретное место на конкретное мероприятие")
    void shouldCheckPlaceOnEvent() {
        Ticket ticketWithExistedPlace = new Ticket(0L, 0L, 0L, Ticket.Category.BAR, 10);

        when(ticketDatabase.selectByEventIdAndPlace(ticketWithExistedPlace.getEventId(), ticketWithExistedPlace.getPlace()))
                .thenReturn(ticketWithExistedPlace);

        assertTrue(ticketRepository.existByPlace(new Ticket(1L, 0L, 1L, Ticket.Category.BAR, 10)));

        when(ticketDatabase.selectByEventIdAndPlace(any(Long.class), any(Integer.class)))
                .thenReturn(null);

        assertFalse(ticketRepository.existByPlace(new Ticket(2L, 0L, 2L, Ticket.Category.BAR, 8)));
    }
}