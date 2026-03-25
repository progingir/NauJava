package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valerii.NauJava.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    List<Account> findByBalanceBetweenAndCurrency(BigDecimal minBalance, BigDecimal maxBalance, String currency);
}