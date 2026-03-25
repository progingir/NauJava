package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valerii.NauJava.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}