package com.rntgroup.repository;

import com.rntgroup.db.UserDatabase;
import com.rntgroup.model.User;

import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepository extends AbstractRepository<User, Long> {

    UserDatabase database;

    public SearchResult<User> findByName(String name, Page page) {
        log.debug("Method {}#findByName was called with params: name = {}, page = {}", this.getClass().getSimpleName(), name, page);
        return SearchResult.pack(getDatabase().selectByName(name), page);
    }

    public Optional<User> findByEmail(String email) {
        log.debug("Method {}#findByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return Optional.ofNullable(getDatabase().selectByEmail(email));
    }

}
