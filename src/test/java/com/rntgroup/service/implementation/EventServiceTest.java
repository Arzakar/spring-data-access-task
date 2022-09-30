package com.rntgroup.service.implementation;

import static java.util.Calendar.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rntgroup.TestUtil;
import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;
import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;

import com.rntgroup.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Должен сохранить Event в БД и вернуть сохранённый объект")
    void shouldCreateEvent() {
        Event testEvent = new Event(0L, "TestEvent", new Date());

        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        assertEquals(testEvent, eventService.create(testEvent));
        verify(eventRepository).save(testEvent);
    }

    @Test
    @DisplayName("Должен вернуть Event из БД, найденный по id")
    void shouldReturnEventById() {
        Event testEvent = new Event(0L, "TestEvent", new Date());

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(testEvent));

        assertEquals(testEvent, eventService.findById(0L));
        verify(eventRepository).findById(0L);
    }

    @Test
    @DisplayName("Должен выбросить исключение, т.к. Event с заданным id не найден")
    void shouldThrowExceptionBecauseEventByIdNotFound() {
        RuntimeException expectedException = new RuntimeException("Event with id = 0 not found");

        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> eventService.findById(0L));
        verify(eventRepository).findById(0L);
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Events с одинаковыми названиями с определённой страницы")
    void shouldReturnEventsByTitle() {
        List<Event> testEvents = List.of(new Event(0L, "TestEvent", new Date()),
                new Event(1L, "TestEvent", new Date()));

        when(eventRepository.findByTitle(any(String.class), any(Page.class)))
                .thenReturn(new SearchResult<Event>().setContent(testEvents).setPage(Page.of(2, 1)));

        assertEquals(testEvents, eventService.findByTitle("TestEvent", 2, 1));
        verify(eventRepository).findByTitle("TestEvent", Page.of(2, 1));
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Events с одинаковыми датами с определённой страницы")
    void shouldReturnEventsByDate() {
        Date testDate = TestUtil.createDate(2022, SEPTEMBER, 5);
        List<Event> testEvents = List.of(new Event(0L, "TestEvent_01", testDate),
                new Event(1L, "TestEvent_02", testDate));

        when(eventRepository.findByDate(any(Date.class), any(Page.class)))
                .thenReturn(new SearchResult<Event>().setContent(testEvents).setPage(Page.of(2, 1)));

        assertEquals(testEvents, eventService.findByDate(testDate, 2, 1));
        verify(eventRepository).findByDate(testDate, Page.of(2, 1));
    }

    @Test
    @DisplayName("Должен обновить Event в БД и вернуть обновлённый объект")
    void shouldUpdateEvent() {
        Event testEvent = new Event(2L, "UpdatedTestEvent", new Date());

        when(eventRepository.update(any(Event.class))).thenReturn(testEvent);

        assertEquals(testEvent, eventService.update(testEvent));
        verify(eventRepository).update(testEvent);
    }

    @Test
    @DisplayName("Должен удалить Event из БД и вернуть удалённый объект")
    void shouldDeleteEventById() {
        Event testEvent = new Event(0L, "DeletedTestEvent", new Date());

        when(eventRepository.deleteById(any(Long.class))).thenReturn(testEvent);

        assertEquals(testEvent, eventService.deleteById(0L));
        verify(eventRepository).deleteById(0L);
    }
}