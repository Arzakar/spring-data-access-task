package com.rntgroup.db;

import com.rntgroup.db.util.IdGenerator;
import com.rntgroup.model.Event;

import lombok.AccessLevel;
import lombok.Getter;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDatabase extends AbstractDatabase<Long, Event> {

    Map<Long, Event> data = new HashMap<>();

    public List<Event> selectByTitle(String title) {
        log.debug("Method {}#selectByTitle was called with param: title = {}", this.getClass().getSimpleName(), title);
        return getData().values().stream()
                .filter(event -> event.getTitle().equals(title))
                .collect(Collectors.toList());
    }

    public List<Event> selectByDate(Date date) {
        log.debug("Method {}#selectByDate was called with param: date = {}", this.getClass().getSimpleName(), date);
        return getData().values().stream()
                .filter(event -> event.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public Long generateId() {
        return IdGenerator.generate(data);
    }
}
