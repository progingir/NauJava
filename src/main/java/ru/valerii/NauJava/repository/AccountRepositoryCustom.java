package ru.valerii.NauJava.repository;

import ru.valerii.NauJava.entity.Account;
import java.util.List;

public interface AccountRepositoryCustom {
    List<Account> searchAccountsByCriteria(Double min, Double max, String currency);
}