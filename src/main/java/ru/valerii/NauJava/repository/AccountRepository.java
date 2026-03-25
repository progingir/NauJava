package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.valerii.NauJava.entity.Account;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    List<Account> findByBalanceBetweenAndCurrency(BigDecimal minBalance, BigDecimal maxBalance, String currency);
}