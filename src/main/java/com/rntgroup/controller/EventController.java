package com.rntgroup.controller;

import com.rntgroup.dto.EventDto;
import com.rntgroup.exception.BadRequestException;
import com.rntgroup.facade.BookingFacade;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    BookingFacade bookingFacade;

    @PostMapping
    public EventDto createEvent(@RequestBody EventDto eventDto) {
        return bookingFacade.createEvent(eventDto);
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable("id") UUID id) {
        return bookingFacade.getEventById(id);
    }

    @PatchMapping("/{id}")
    public EventDto updateEvent(@PathVariable("id") UUID eventId,
                                @RequestBody EventDto eventDto) {
        return bookingFacade.updateEvent(eventDto.setId(eventId));
    }

    @DeleteMapping("/{id}")
    public boolean deleteEvent(@PathVariable("id") UUID eventId) {
        return bookingFacade.deleteEvent(eventId);
    }

    @GetMapping("/search")
    public List<EventDto> getEvents(@RequestParam(name = "title", required = false) String title,
                                    @RequestParam(name = "day", required = false) LocalDate day,
                                    @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                    @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(name = "direction", required = false, defaultValue = "ASC") Sort.Direction direction,
                                    @RequestParam(name = "sortParam", required = false, defaultValue = "id") String sortParam) {

        boolean titleIsNull = Objects.isNull(title);
        boolean dayIsNull = Objects.isNull(day);

        List<Boolean> requestParamsNullableFlags = List.of(
                titleIsNull,
                dayIsNull
        );

        long notNullParamsCount = requestParamsNullableFlags.stream()
                .filter(value -> value.equals(Boolean.FALSE))
                .count();

        if (notNullParamsCount != 1) {
            throw new BadRequestException("Ошибка в запросе: указано неверное количество параметров поиска");
        }

        if (!titleIsNull) {
            return bookingFacade.getEventsByTitle(title, pageNum, pageSize, direction, sortParam);
        }

        return bookingFacade.getEventsForDay(day, pageNum, pageSize, direction, sortParam);
    }

}
