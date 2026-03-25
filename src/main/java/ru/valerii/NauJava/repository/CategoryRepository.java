package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.valerii.NauJava.entity.Category;

@RepositoryRestResource(path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}