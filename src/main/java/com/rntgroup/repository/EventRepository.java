package com.rntgroup.repository;

import com.rntgroup.db.EventDatabase;
import com.rntgroup.model.Event;
import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventRepository extends AbstractRepository<Event, Long> {

    static final Logger LOG = LoggerFactory.getLogger(EventRepository.class.getSimpleName());

    EventDatabase database;

    public SearchResult<Event> findByTitle(String title, Page page) {
        LOG.debug("Method {}#findByTitle was called with params: title = {}, page = {}", this.getClass().getSimpleName(), title, page);
        return SearchResult.pack(getDatabase().selectByTitle(title), page);
    }

    public SearchResult<Event> findByDate(Date date, Page page) {
        LOG.debug("Method {}#findByDate was called with params: date = {}, page = {}", this.getClass().getSimpleName(), date, page);
        return SearchResult.pack(getDatabase().selectByDate(date), page);
    }
}
