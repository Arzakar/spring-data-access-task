package com.rntgroup.db;

import com.rntgroup.db.util.IdGenerator;
import com.rntgroup.model.Ticket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketDatabase extends AbstractDatabase<Long, Ticket> {

    Map<Long, Ticket> data = new HashMap<>();

    final UserDatabase userDatabase;
    final EventDatabase eventDatabase;

    public List<Ticket> selectByEventId(long eventId) {
        log.debug("Method {}#selectByEventId was called with param: eventId = {}", this.getClass().getSimpleName(), eventId);
        return getData().values().stream()
                .filter(ticket -> ticket.getEventId() == eventId)
                .sorted((ticket1, ticket2) -> {
                    String userEmail1 = userDatabase.selectById(ticket1.getUserId()).getEmail();
                    String userEmail2 = userDatabase.selectById(ticket2.getUserId()).getEmail();
                    return userEmail1.compareTo(userEmail2);
                })
                .collect(Collectors.toList());
    }

    public List<Ticket> selectByUserId(long userId) {
        log.debug("Method {}#selectByUserId was called with param: userId = {}", this.getClass().getSimpleName(), userId);
        return getData().values().stream()
                .filter(ticket -> ticket.getUserId() == userId)
                .sorted((ticket1, ticket2) -> {
                    Date eventDate1 = eventDatabase.selectById(ticket1.getEventId()).getDate();
                    Date eventDate2 = eventDatabase.selectById(ticket2.getEventId()).getDate();
                    return eventDate2.compareTo(eventDate1);
                })
                .collect(Collectors.toList());
    }

    public Ticket selectByEventIdAndPlace(long eventId, int place) {
        log.debug("Method {}#selectByEventIdAndPlace was called with params: eventId = {}, place = {}",
                this.getClass().getSimpleName(), eventId, place);
        return getData().values().stream()
                .filter(ticket -> ticket.getEventId() == eventId && ticket.getPlace() == place)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Long generateId() {
        return IdGenerator.generate(data);
    }
}
