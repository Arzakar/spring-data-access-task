package com.rntgroup.repository;

import com.rntgroup.db.TicketDatabase;
import com.rntgroup.model.Ticket;
import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRepository extends AbstractRepository<Ticket, Long> {

    static final Logger LOG = LoggerFactory.getLogger(TicketRepository.class.getSimpleName());

    TicketDatabase database;

    public SearchResult<Ticket> findByUserId(long userId, Page page) {
        LOG.debug("Method {}#findByUserId was called with params: userId = {}, page = {}", this.getClass().getSimpleName(), userId, page);
        return SearchResult.pack(getDatabase().selectByUserId(userId), page);
    }

    public SearchResult<Ticket> findByEventId(long eventId, Page page) {
        LOG.debug("Method {}#findByEventId was called with params: eventId = {}, page = {}", this.getClass().getSimpleName(), eventId, page);
        return SearchResult.pack(getDatabase().selectByEventId(eventId), page);
    }

    public boolean existByPlace(Ticket ticket) {
        LOG.debug("Method {}#existByPlace was called with param: ticket = {}", this.getClass().getSimpleName(), ticket);
        return Objects.nonNull(getDatabase().selectByEventIdAndPlace(ticket.getEventId(), ticket.getPlace()));
    }
}
