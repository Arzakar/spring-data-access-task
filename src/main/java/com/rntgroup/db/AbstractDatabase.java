package com.rntgroup.db;

import com.rntgroup.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class AbstractDatabase<ID, T extends Entity<ID>> implements Database<ID, T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDatabase.class.getSimpleName());

    @Override
    public T insert(T entity) {
        LOG.debug("Method {}#insert was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        ID id = generateId();
        entity.setId(id);
        getData().put(id, entity);

        return entity;
    }

    @Override
    public T selectById(ID id) {
        LOG.info("Method {}#selectById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return getData().get(id);
    }

    @Override
    public T update(T entity) {
        LOG.info("Method {}#update was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        ID id = entity.getId();

        if (Objects.nonNull(getData().get(id))) {
            getData().put(id, entity);
            return entity;
        }

        return null;
    }

    public T deleteById(ID id) {
        LOG.info("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return getData().remove(id);
    }

    @Override
    public long getSize() {
        return getData().size();
    }
}
