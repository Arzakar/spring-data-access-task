package com.rntgroup.repository;

import com.rntgroup.model.Entity;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class AbstractRepository<T extends Entity<ID>, ID> implements Repository<T, ID> {

    @Override
    public T save(T entity) {
        log.debug("Method {}#save was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        return getDatabase().insert(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        log.debug("Method {}#findById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return Optional.ofNullable(getDatabase().selectById(id));
    }

    @Override
    public T update(T entity) {
        log.debug("Method {}#update was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        return getDatabase().update(entity);
    }

    @Override
    public T deleteById(ID id) {
        log.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return getDatabase().deleteById(id);
    }
}




