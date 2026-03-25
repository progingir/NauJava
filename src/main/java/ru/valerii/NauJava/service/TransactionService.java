package ru.valerii.NauJava.service;

import ru.valerii.NauJava.entity.Transaction;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Long save(BigDecimal amount, String currency, String description);

    void update(Long id, BigDecimal amount, String currency, String description);

    List<Transaction> getAll();

    void remove(Long id);

    BigDecimal calculateTotalAmount(String targetCurrencyCode);
}