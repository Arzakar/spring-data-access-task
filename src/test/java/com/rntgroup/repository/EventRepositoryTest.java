package com.rntgroup.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestUtil;
import com.rntgroup.db.EventDatabase;
import com.rntgroup.model.Event;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class EventRepositoryTest {

    @Mock
    private EventDatabase eventDatabase;

    @InjectMocks
    private EventRepository eventRepository;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    @Test
    @DisplayName("Должен сохранить Event в БД и вернуть сохранённый объект")
    void shouldSaveEvent() {
        Event expectedResult = new Event(0L, "TestEvent", new Date());

        when(eventDatabase.insert(expectedResult)).thenReturn(expectedResult);

        assertEquals(expectedResult, eventRepository.save(expectedResult));
        verify(eventDatabase).insert(expectedResult);
    }

    @ParameterizedTest(name = "[{index}] Event id = {0}, response by DB = {1}, expected result = {2}")
    @MethodSource("getEventById")
    @DisplayName("Должен вернуть Optional с найденным по id Event или Optional.empty")
    void shouldReturnEventById(Long id, Event responseEvent, Optional<Event> expectedResult) {
        when(eventDatabase.selectById(id)).thenReturn(responseEvent);

        assertEquals(expectedResult, eventRepository.findById(id));
        verify(eventDatabase).selectById(id);
    }

    private static Stream<Arguments> getEventById() {
        return Stream.of(
                Arguments.of(0L, new Event(0L, "TestEvent", new Date()), Optional.of(new Event(0L, "TestEvent", new Date()))),
                Arguments.of(10L, null, Optional.empty())
        );
    }

    @Test
    @DisplayName("Должен обновить запись в БД и вернуть изменённый объект")
    void shouldUpdateEvent() {
        Event expectedResult = new Event(0L, "TestEvent", new Date());

        when(eventDatabase.update(expectedResult)).thenReturn(expectedResult);

        assertEquals(expectedResult, eventRepository.update(expectedResult));
        verify(eventDatabase).update(expectedResult);
    }

    @Test
    @DisplayName("Должен удалить запись из БД с конкретным id и вернуть удалённый объект")
    void shouldDeleteById() {
        Event expectedResult = new Event(0L, "TestEvent", new Date());

        when(eventDatabase.deleteById(0L)).thenReturn(expectedResult);

        assertEquals(expectedResult, eventRepository.deleteById(0L));
        verify(eventDatabase).deleteById(0L);
    }

    @ParameterizedTest(name = "[{index}] Page = {1}, expected search result = {2}")
    @MethodSource("getEventsByTitle")
    @DisplayName("Должен вернуть определённую страницу с результатом поиска по названию")
    void shouldReturnSearchResultByTitle(List<Event> responseEventsByTitle, Page page, SearchResult<Event> expectedResult) {
        when(eventDatabase.selectByTitle(any(String.class))).thenReturn(responseEventsByTitle);

        SearchResult<Event> actualResult = eventRepository.findByTitle("КВН", page);

        assertEquals(expectedResult, actualResult);
    }

    @SneakyThrows
    private static Stream<Arguments> getEventsByTitle() {
        List<Event> responseEventsByTitle = OBJECT_MAPPER.readValue(
                TestUtil.readResourceAsString("repository/response/events-by-title.json"),
                new TypeReference<>() {
                }
        );

        return Stream.of(
                Arguments.of(responseEventsByTitle, Page.of(3, 1), SearchResult.pack(responseEventsByTitle, Page.of(3, 1))),
                Arguments.of(responseEventsByTitle, Page.of(2, 3), SearchResult.pack(responseEventsByTitle, Page.of(2, 3))),
                Arguments.of(responseEventsByTitle, Page.of(4, 2), SearchResult.pack(responseEventsByTitle, Page.of(4, 2)))
        );
    }

    @ParameterizedTest(name = "[{index}] Page = {1}, expected search result = {2}")
    @MethodSource("getEventsByDate")
    @DisplayName("Должен вернуть определённую страницу с результатом поиска по дате")
    void shouldReturnSearchResultByDate(List<Event> responseEventsByDate, Page page, SearchResult<Event> expectedResult) {
        when(eventDatabase.selectByDate(any(Date.class))).thenReturn(responseEventsByDate);

        SearchResult<Event> actualResult = eventRepository.findByDate(new Date(), page);

        assertEquals(expectedResult, actualResult);
    }

    @SneakyThrows
    private static Stream<Arguments> getEventsByDate() {
        List<Event> responseEventsByDate = OBJECT_MAPPER.readValue(
                TestUtil.readResourceAsString("repository/response/events-by-date.json"),
                new TypeReference<>() {
                }
        );

        return Stream.of(
                Arguments.of(responseEventsByDate, Page.of(3, 1), SearchResult.pack(responseEventsByDate, Page.of(3, 1))),
                Arguments.of(responseEventsByDate, Page.of(2, 3), SearchResult.pack(responseEventsByDate, Page.of(2, 3))),
                Arguments.of(responseEventsByDate, Page.of(4, 2), SearchResult.pack(responseEventsByDate, Page.of(4, 2)))
        );
    }

}