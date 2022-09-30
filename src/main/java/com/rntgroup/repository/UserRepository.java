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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepository extends AbstractRepository<User, Long> {

    static final Logger LOG = LoggerFactory.getLogger(UserRepository.class.getSimpleName());

    UserDatabase database;

    public SearchResult<User> findByName(String name, Page page) {
        LOG.debug("Method {}#findByName was called with params: name = {}, page = {}", this.getClass().getSimpleName(), name, page);
        return SearchResult.pack(getDatabase().selectByName(name), page);
    }

    public Optional<User> findByEmail(String email) {
        LOG.debug("Method {}#findByEmail was called with param: email = {}", this.getClass().getSimpleName(), email);
        return Optional.ofNullable(getDatabase().selectByEmail(email));
    }

}
