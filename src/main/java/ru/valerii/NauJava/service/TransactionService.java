package ru.valerii.NauJava.service;

import ru.valerii.NauJava.entity.Transaction;
import java.util.List;

public interface TransactionService {
    void save(Long id, Double amount, String currency, String description);

    List<Transaction> getAll();

    Double calculateTotalAmount();

    void remove(Long id);
}