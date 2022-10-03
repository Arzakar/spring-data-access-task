package com.rntgroup.controller;

import com.rntgroup.exception.BadRequestException;
import com.rntgroup.exception.NotImplementedException;
import com.rntgroup.facade.BookingFacade;
import com.rntgroup.model.Event;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    BookingFacade bookingFacade;

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") long id) {
        return bookingFacade.getEventById(id);
    }

    @GetMapping
    public List<Event> getEvents(@RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "day", required = false) Date day,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {

        if (Objects.nonNull(title) && Objects.nonNull(day)) {
            throw new BadRequestException("Ошибка в запросе: title и day не могут задаваться одновременно");
        }

        if (Objects.isNull(title) && Objects.isNull(day)) {
            throw new NotImplementedException("Метод получения всех мероприятий не реализован");
        }

        if (Objects.nonNull(title)) {
            return bookingFacade.getEventsByTitle(title, pageSize, pageNum);
        }

        return bookingFacade.getEventsForDay(day, pageSize, pageNum);
    }

    @PostMapping
    public Event createEvent(Event event) {
        return bookingFacade.createEvent(event);
    }

    @PatchMapping
    public Event updateEvent(Event event) {
        return bookingFacade.updateEvent(event);
    }

    @DeleteMapping("/{id}")
    public boolean deleteEvent(@PathVariable("id") long eventId) {
        return bookingFacade.deleteEvent(eventId);
    }
}
