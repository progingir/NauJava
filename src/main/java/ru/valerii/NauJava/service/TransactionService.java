package ru.valerii.NauJava.service;

import ru.valerii.NauJava.entity.FinancialTransaction;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Long save(Long accountId, BigDecimal amount, String description);

    void update(Long id, BigDecimal amount, String description);

    List<FinancialTransaction> getAll();

    void remove(Long id);

    BigDecimal calculateTotalAmount(String targetCurrencyCode);
}