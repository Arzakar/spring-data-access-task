package com.rntgroup.repository;

import com.rntgroup.db.TicketDatabase;
import com.rntgroup.model.Ticket;
import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketRepository extends AbstractRepository<Ticket, Long> {

    TicketDatabase database;

    public SearchResult<Ticket> findByUserId(long userId, Page page) {
        log.debug("Method {}#findByUserId was called with params: userId = {}, page = {}", this.getClass().getSimpleName(), userId, page);
        return SearchResult.pack(getDatabase().selectByUserId(userId), page);
    }

    public SearchResult<Ticket> findByEventId(long eventId, Page page) {
        log.debug("Method {}#findByEventId was called with params: eventId = {}, page = {}", this.getClass().getSimpleName(), eventId, page);
        return SearchResult.pack(getDatabase().selectByEventId(eventId), page);
    }

    public boolean existByPlace(Ticket ticket) {
        log.debug("Method {}#existByPlace was called with param: ticket = {}", this.getClass().getSimpleName(), ticket);
        return Objects.nonNull(getDatabase().selectByEventIdAndPlace(ticket.getEventId(), ticket.getPlace()));
    }
}
