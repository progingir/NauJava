package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.valerii.NauJava.entity.Bank;

@RepositoryRestResource(path = "banks")
public interface BankRepository extends JpaRepository<Bank, Long> {
}