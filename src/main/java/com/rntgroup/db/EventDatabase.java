package com.rntgroup.db;

import com.rntgroup.db.util.IdGenerator;
import com.rntgroup.model.Event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDatabase extends AbstractDatabase<Long, Event> {

    static final Logger LOG = LoggerFactory.getLogger(EventDatabase.class.getSimpleName());

    Map<Long, Event> data = new HashMap<>();

    public List<Event> selectByTitle(String title) {
        LOG.debug("Method {}#selectByTitle was called with param: title = {}", this.getClass().getSimpleName(), title);
        return getData().values().stream()
                .filter(event -> event.getTitle().equals(title))
                .collect(Collectors.toList());
    }

    public List<Event> selectByDate(Date date) {
        LOG.debug("Method {}#selectByDate was called with param: date = {}", this.getClass().getSimpleName(), date);
        return getData().values().stream()
                .filter(event -> event.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public Long generateId() {
        return IdGenerator.generate(data);
    }
}
