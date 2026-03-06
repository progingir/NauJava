package ru.valerii.NauJava.service;

import ru.valerii.NauJava.entity.Transaction;
import java.util.List;

public interface TransactionService {
    void save(Double amount, String currency, String description);

    void update(Long id, Double amount, String currency, String description);

    List<Transaction> getAll();

    void remove(Long id);

    Double calculateTotalAmount(String targetCurrencyCode);
}