package ru.valerii.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.valerii.NauJava.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}