package ru.valerii.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.valerii.NauJava.entity.Account;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long>, AccountRepositoryCustom {
    List<Account> findByBalanceBetweenAndCurrency(Double minBalance, Double maxBalance, String currency);
}