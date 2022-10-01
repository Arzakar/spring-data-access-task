package com.rntgroup.db;

import com.rntgroup.db.util.IdGenerator;
import com.rntgroup.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDatabase extends AbstractDatabase<Long, User>{

    Map<Long, User> data = new HashMap<>();

    public List<User> selectByName(String name) {
        log.debug("Method {}#selectByName was called with param: name = {}", this.getClass().getSimpleName(), name);
        return getData().values().stream()
                .filter(user -> user.getName().equals(name))
                .collect(Collectors.toList());
    }

    public User selectByEmail(String email) {
        log.debug("Method {}#selectByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return getData().values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Long generateId() {
        return IdGenerator.generate(data);
    }
}
