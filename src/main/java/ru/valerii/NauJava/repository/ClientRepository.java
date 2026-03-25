package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.valerii.NauJava.entity.Client;

@RepositoryRestResource(path = "clients")
public interface ClientRepository extends JpaRepository<Client, Long> {
}