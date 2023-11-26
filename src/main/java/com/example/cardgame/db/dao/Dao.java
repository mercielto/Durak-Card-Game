package com.example.cardgame.db.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();
    T getById(Long id);
    void update(T entity);
    boolean delete(Long id);
    boolean insert(T entity);
}
