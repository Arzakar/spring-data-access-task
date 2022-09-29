package com.rntgroup.service.implementation;

import com.rntgroup.model.Event;
import com.rntgroup.repository.EventRepository;
import com.rntgroup.repository.util.Page;
import com.rntgroup.service.EventService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class.getSimpleName());

    EventRepository eventRepository;

    @Override
    public Event create(Event event) {
        LOG.debug("Method {}#create was called with param: event = {}", this.getClass().getSimpleName(), event);
        return eventRepository.save(event);
    }

    @Override
    public Event findById(long id) {
        LOG.debug("Method {}#findById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return eventRepository.findById(id).orElseThrow(() -> {
                    var error = new RuntimeException(String.format("Event with id = %d not found", id));
                    LOG.error("Method {}#findById returned error with message: {}", this.getClass().getSimpleName(), error.getMessage());
                    return error;
                });
    }

    @Override
    public List<Event> findByTitle(String title, int pageSize, int pageNum) {
        LOG.debug("Method {}#findByTitle was called with params: title = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), title, pageSize, pageNum);
        return eventRepository.findByTitle(title, Page.of(pageSize, pageNum))
                .getContent();
    }

    @Override
    public List<Event> findByDate(Date date, int pageSize, int pageNum) {
        LOG.debug("Method {}#findByDate was called with params: date = {}, pageSize = {}, pageNum = {}",
                this.getClass().getSimpleName(), date, pageSize, pageNum);
        return eventRepository.findByDate(date, Page.of(pageSize, pageNum))
                .getContent();
    }

    @Override
    public Event update(Event event) {
        LOG.debug("Method {}#update was called with param: event = {}", this.getClass().getSimpleName(), event);
        return eventRepository.update(event);
    }

    @Override
    public Event deleteById(long eventId) {
        LOG.debug("Method {}#deleteById was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);
        return eventRepository.deleteById(eventId);
    }
}
