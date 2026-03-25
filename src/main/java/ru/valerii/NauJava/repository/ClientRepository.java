package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valerii.NauJava.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}