package ru.valerii.NauJava.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    void create(T entity);
    Optional<T> read(Long id);
    List<T> readAll();
    void update(T entity);
    void delete(Long id);
}