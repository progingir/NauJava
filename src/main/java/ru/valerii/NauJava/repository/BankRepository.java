package ru.valerii.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.valerii.NauJava.entity.Bank;

public interface BankRepository extends CrudRepository<Bank, Long> {
}