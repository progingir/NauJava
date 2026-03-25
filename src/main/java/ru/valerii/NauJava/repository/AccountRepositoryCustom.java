package ru.valerii.NauJava.repository;

import ru.valerii.NauJava.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepositoryCustom {
    List<Account> searchAccountsByCriteria(BigDecimal min, BigDecimal max, String currency);
}