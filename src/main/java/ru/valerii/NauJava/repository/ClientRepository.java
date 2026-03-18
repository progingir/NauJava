package ru.valerii.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.valerii.NauJava.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}