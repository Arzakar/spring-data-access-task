package com.rntgroup.repository;

import com.rntgroup.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class AbstractRepository<T extends Entity<ID>, ID> implements Repository<T, ID> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRepository.class.getSimpleName());

    @Override
    public T save(T entity) {
        LOG.debug("Method {}#save was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        return getDatabase().insert(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        LOG.debug("Method {}#findById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return Optional.ofNullable(getDatabase().selectById(id));
    }

    @Override
    public T update(T entity) {
        LOG.debug("Method {}#update was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        return getDatabase().update(entity);
    }

    @Override
    public T deleteById(ID id) {
        LOG.debug("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return getDatabase().deleteById(id);
    }
}
