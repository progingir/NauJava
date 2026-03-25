package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valerii.NauJava.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {
}