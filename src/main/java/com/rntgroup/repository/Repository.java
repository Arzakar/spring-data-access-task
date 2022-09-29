package com.rntgroup.repository;

import com.rntgroup.db.Database;

import java.util.Optional;

public interface Repository<T, ID> {

    Database<ID, T> getDatabase();

    T save(T t);

    Optional<T> findById(ID id);

    T update(T t);

    T deleteById(ID id);
}
