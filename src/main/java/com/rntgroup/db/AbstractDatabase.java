package com.rntgroup.db;

import com.rntgroup.model.Entity;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class AbstractDatabase<ID, T extends Entity<ID>> implements Database<ID, T> {

    @Override
    public T insert(T entity) {
        log.debug("Method {}#insert was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        ID id = generateId();
        entity.setId(id);
        getData().put(id, entity);

        return entity;
    }

    @Override
    public T selectById(ID id) {
        log.info("Method {}#selectById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return getData().get(id);
    }

    @Override
    public T update(T entity) {
        log.info("Method {}#update was called with param: entity = {}", this.getClass().getSimpleName(), entity);
        ID id = entity.getId();

        if (Objects.nonNull(getData().get(id))) {
            getData().put(id, entity);
            return entity;
        }

        return null;
    }

    public T deleteById(ID id) {
        log.info("Method {}#deleteById was called with param: id = {}", this.getClass().getSimpleName(), id);
        return getData().remove(id);
    }

    @Override
    public long getSize() {
        return getData().size();
    }
}
