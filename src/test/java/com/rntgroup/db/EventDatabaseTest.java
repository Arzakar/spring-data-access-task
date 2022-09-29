package com.rntgroup.db;

import static java.util.Calendar.JANUARY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.MAY;
import static java.util.Calendar.NOVEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestUtil;
import com.rntgroup.model.Event;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class EventDatabaseTest {

    private final EventDatabase eventDatabase = new EventDatabase();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        Map<Long, Event> testEvents = objectMapper.readValue(
                TestUtil.readResourceAsString("db/init-event-data.json"),
                new TypeReference<>() {}
        );
        eventDatabase.setData(testEvents);
    }

    @Test
    @DisplayName("Должен сохранить Event в базе и проставить уникальный id независимо от того, какой был передан")
    void shouldCreateEvent() {
        Event cinemaEvent = new Event(null, "Фильм: Доктор Стрэндж", TestUtil.createDate(2022, 5, 12));
        Event learningEvent = new Event(5L, "Обучение рукопашному бою", TestUtil.createDate(2022, 8, 30));

        Event actualCinemaEventResult = eventDatabase.insert(cinemaEvent);

        assertEquals(new Event(3L, cinemaEvent.getTitle(), cinemaEvent.getDate()), actualCinemaEventResult);
        assertEquals(eventDatabase.getData().get(3L), actualCinemaEventResult);

        Event actualLearningEventResult = eventDatabase.insert(learningEvent);

        assertEquals(new Event(7L, learningEvent.getTitle(), learningEvent.getDate()), actualLearningEventResult);
        assertEquals(eventDatabase.getData().get(7L), actualLearningEventResult);
    }

    @ParameterizedTest(name = "[{index}] Event id = {0}, events with this id = {1}")
    @MethodSource("getIds")
    @DisplayName("Должен вернуть Event из БД с конкретным id или null, если такого Event в ней нет")
    void shouldReturnEventById(Long id, Event expectedResult) {
        Event actualResult = eventDatabase.selectById(id);

        assertEquals(expectedResult, actualResult);
    }

    private static Stream<Arguments> getIds() {
        return Stream.of(
                Arguments.of(0L, new Event(0L, "Концерт симфонической музыки", TestUtil.createDate(2022, NOVEMBER, 15))),
                Arguments.of(4L, new Event(4L, "КВН", TestUtil.createDate(2023, JANUARY, 15))),
                Arguments.of(10L, null)
        );
    }

    @Test
    @DisplayName("Должен обновить запись в таблице")
    void shouldUpdateEvent() {
        Event changedEvent = new Event(4L, "КВН. Молодёжная лига", TestUtil.createDate(2023, JANUARY, 15));
        Event actualResult = eventDatabase.update(changedEvent);

        assertEquals(changedEvent, actualResult);
        assertEquals(eventDatabase.getData().get(4L), changedEvent);

    }

    @Test
    @DisplayName("Не должен обновить запись в таблице, т.к. записи с таким id нет")
    void shouldNotUpdateEvent() {
        Event changedEvent = new Event(10L, "КВН. Молодёжная лига", TestUtil.createDate(2023, JANUARY, 15));
        Event actualResult = eventDatabase.update(changedEvent);

        assertNull(actualResult);
        assertFalse(eventDatabase.getData().containsKey(10L));
        assertFalse(eventDatabase.getData().containsValue(changedEvent));

    }

    @Test
    @DisplayName("Должен удалить запись с БД")
    void shouldDeleteEventById() {
        long originSize = eventDatabase.getSize();

        Event deletedEvent = eventDatabase.deleteById(4L);

        assertEquals(new Event(4L, "КВН", TestUtil.createDate(2023, JANUARY, 15)), deletedEvent);
        assertEquals(originSize - 1, eventDatabase.getSize());
        assertFalse(eventDatabase.getData().containsKey(4L));
        assertFalse(eventDatabase.getData().containsValue(deletedEvent));
    }

    @Test
    @DisplayName("Не должен удалить запись с БД, т.к. записи с таким id в ней нет")
    void shouldNotDeleteEventById() {
        long originSize = eventDatabase.getSize();

        Event deletedEvent = eventDatabase.deleteById(10L);

        assertNull(deletedEvent);
        assertEquals(originSize, eventDatabase.getSize());
    }

    @ParameterizedTest(name = "[{index}] Event title = {0}, events with this title = {1}")
    @MethodSource("getTitles")
    @DisplayName("Должен вернуть все Event из БД с конкретным именем")
    void shouldReturnEventsByTitle(String title, int expectedListSize) {
        List<Event> actualResult = eventDatabase.selectByTitle(title);

        assertEquals(expectedListSize, actualResult.size());
        assertTrue(actualResult.stream().allMatch(event -> event.getTitle().equals(title)));
    }

    private static Stream<Arguments> getTitles() {
        return Stream.of(
                Arguments.of("КВН", 2),
                Arguments.of("Концерт Three Days Grace", 1),
                Arguments.of("Экскурсия в музей машин", 0)
        );
    }

    @ParameterizedTest(name = "[{index}] Event date = {0}, events on this date = {1}")
    @MethodSource("getDates")
    @DisplayName("Должен вернуть все Event из БД на конкретную дату")
    void shouldReturnEventsByDate(Date date, int expectedListSize) {
        List<Event> actualResult = eventDatabase.selectByDate(date);

        assertEquals(expectedListSize, actualResult.size());
        assertTrue(actualResult.stream().allMatch(event -> event.getDate().equals(date)));
    }

    private static Stream<Arguments> getDates() {
        return Stream.of(
                Arguments.of(TestUtil.createDate(2022, NOVEMBER, 15), 2),
                Arguments.of(TestUtil.createDate(2022, MAY, 7), 1),
                Arguments.of(TestUtil.createDate(2023, JUNE, 23), 0)
        );
    }
}