package ru.valerii.NauJava.repository;

import java.util.List;

public interface CrudRepository<T> {
    void create(T entity);
    T read(Long id);
    List<T> readAll();
    void update(T entity);
    void delete(Long id);
}