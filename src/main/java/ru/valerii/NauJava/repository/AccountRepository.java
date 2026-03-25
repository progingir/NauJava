package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valerii.NauJava.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    List<Account> findByBalanceBetweenAndCurrency(Double minBalance, Double maxBalance, String currency);
}