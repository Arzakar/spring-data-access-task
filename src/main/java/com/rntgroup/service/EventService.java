package com.rntgroup.service;

import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {

    EventRepository eventRepository;

    public Event create(Event event) {
        log.debug("Method {}#create was called with param: event = {}", this.getClass().getSimpleName(), event);

        return eventRepository.save(event);
    }

    public Event findById(UUID id) {
        log.debug("Method {}#findById was called with param: id = {}", this.getClass().getSimpleName(), id);

        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Event with id = %s not found", id)));
    }

    public List<Event> findByTitle(String title, Pageable pageable) {
        log.debug("Method {}#findByTitle was called with params: title = {}, pageable = {}",
                this.getClass().getSimpleName(), title, pageable);

        return eventRepository.findByTitle(title, pageable).getContent();
    }

    public List<Event> findByDate(LocalDate date, Pageable pageable) {
        log.debug("Method {}#findByDate was called with params: date = {}, pageable = {}",
                this.getClass().getSimpleName(), date, pageable);

        return eventRepository.findByDate(date, pageable).getContent();
    }

    public Event update(Event event) {
        log.debug("Method {}#update was called with param: event = {}", this.getClass().getSimpleName(), event);

        return eventRepository.save(event);
    }

    public boolean deleteById(UUID id) {
        log.debug("Method {}#deleteById was called with param: eventId = {}", this.getClass().getSimpleName(), id);

        eventRepository.deleteById(id);
        return !eventRepository.existsById(id);
    }
}
