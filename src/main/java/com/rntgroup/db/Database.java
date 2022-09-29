package com.rntgroup.db;

import java.util.Map;

public interface Database<ID, T> {

    Map<ID, T> getData();

    T insert(T t);
    T selectById(ID id);
    T update(T t);
    T deleteById(ID id);

    long getSize();
    ID generateId();
}
