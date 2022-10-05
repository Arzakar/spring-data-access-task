package com.rntgroup.service;

import com.rntgroup.exception.NotFoundException;
import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        return eventRepository.findById(id).orElseThrow(() -> {
                    var error = new NotFoundException(String.format("Event with id = %s not found", id));
                    log.error("Method {}#findById returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
                    return error;
                });
    }

    public List<Event> findByTitle(String title, int pageSize, int pageNum) {
        log.debug("Method {}#findByTitle was called with params: title = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), title, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "title");
        return eventRepository.findByTitle(title, pageRequest).getContent();
    }

    public List<Event> findByDate(Date date, int pageSize, int pageNum) {
        log.debug("Method {}#findByDate was called with params: date = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), date, pageSize, pageNum);

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "date");
        return eventRepository.findByDate(date, pageRequest).getContent();
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
