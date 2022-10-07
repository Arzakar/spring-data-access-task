package com.rntgroup.service;

import static java.util.Calendar.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rntgroup.TestDataUtil;
import com.rntgroup.TestUtil;
import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Должен сохранить Event в БД и вернуть сохранённый объект")
    void shouldCreateEvent() {
        Event event = TestDataUtil.getRandomEvent();

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        assertEquals(event, eventService.create(event));
        verify(eventRepository).save(event);
    }

    @Test
    @DisplayName("Должен вернуть Event из БД, найденный по id")
    void shouldReturnEventById() {
        UUID id = UUID.randomUUID();
        Event event = TestDataUtil.getRandomEvent(id);

        when(eventRepository.findById(any(UUID.class))).thenReturn(Optional.of(event));

        assertEquals(event, eventService.findById(id));
        verify(eventRepository).findById(id);
    }

    @Test
    @DisplayName("Должен выбросить исключение, т.к. Event с заданным id не найден")
    void shouldThrowExceptionBecauseEventByIdNotFound() {
        UUID id = UUID.randomUUID();
        NotFoundException expectedException = new NotFoundException(String.format("Event with id = %s not found", id));

        when(eventRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventService.findById(id));
        verify(eventRepository).findById(id);
        assertEquals(expectedException.getMessage(), thrown.getMessage());
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Events с одинаковыми названиями с определённой страницы")
    void shouldReturnEventsByTitle() {
        String title = "Some";
        List<Event> events = List.of(
                new Event().setTitle(title),
                new Event().setTitle(title)
        );
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "title");

        when(eventRepository.findByTitle(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(events, pageRequest, events.size()));

        assertEquals(events, eventService.findByTitle(title,  pageRequest.getPageSize(), pageRequest.getPageNumber()));
        verify(eventRepository).findByTitle(title, pageRequest);
    }

    @Test
    @DisplayName("Должен вернуть список, содержащий Events с одинаковыми датами с определённой страницы")
    void shouldReturnEventsByDate() {
        Date date = TestUtil.createDate(2022, SEPTEMBER, 5);
        List<Event> events = List.of(
                new Event().setDate(date),
                new Event().setDate(date)
        );
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "date");

        when(eventRepository.findByDate(any(Date.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(events, pageRequest, events.size()));

        assertEquals(events, eventService.findByDate(date, pageRequest.getPageSize(), pageRequest.getPageNumber()));
        verify(eventRepository).findByDate(date, pageRequest);
    }

    @Test
    @DisplayName("Должен обновить Event в БД и вернуть обновлённый объект")
    void shouldUpdateEvent() {
        Event event = TestDataUtil.getRandomEvent();

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        assertEquals(event, eventService.update(event));
        verify(eventRepository).save(event);
    }

    @Test
    @DisplayName("Должен удалить Event из БД и вернуть удалённый объект")
    void shouldDeleteEventById() {
        UUID id = UUID.randomUUID();

        when(eventRepository.existsById(any(UUID.class))).thenReturn(false);

        assertTrue(eventService.deleteById(id));
        verify(eventRepository).deleteById(id);
    }
}