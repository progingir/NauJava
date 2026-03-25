package ru.valerii.NauJava.service;

import org.springframework.stereotype.Service;
import ru.valerii.NauJava.entity.Currency;
import ru.valerii.NauJava.entity.Transaction;
import ru.valerii.NauJava.exception.TransactionNotFoundException;
import ru.valerii.NauJava.exception.TransactionValidationException;
import ru.valerii.NauJava.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final CrudRepository<Transaction> repository;

    public TransactionServiceImpl(CrudRepository<Transaction> repository) {
        this.repository = repository;
    }

    @Override
    public Long save(BigDecimal amount, String currencyCode, String description) {
        validateTransactionData(amount, currencyCode);

        Transaction t = new Transaction(null, amount, Currency.valueOf(currencyCode.toUpperCase()), description);

        repository.create(t);

        return t.getId();
    }

    @Override
    public void update(Long id, BigDecimal amount, String currencyCode, String description) {
        Transaction existing = repository.read(id)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция с ID " + id + " не найдена."));

        validateTransactionData(amount, currencyCode);

        existing.setAmount(amount);
        existing.setCurrency(Currency.valueOf(currencyCode.toUpperCase()));
        existing.setDescription(description);

        repository.update(existing);
    }

    @Override
    public List<Transaction> getAll() {
        return repository.readAll();
    }

    @Override
    public BigDecimal calculateTotalAmount(String targetCurrencyCode) {
        Currency targetCurrency = Currency.valueOf(targetCurrencyCode.toUpperCase());

        return repository.readAll().stream()
                .map(t -> t.getCurrency().convertTo(targetCurrency, t.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void remove(Long id) {
        repository.read(id)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция с ID " + id + " не найдена."));

        repository.delete(id);
    }

    private void validateTransactionData(BigDecimal amount, String currencyCode) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionValidationException("Сумма транзакции должна быть больше нуля.");
        }
        if (!Currency.isValid(currencyCode)) {
            throw new TransactionValidationException("Неверная валюта! Доступны: " + Arrays.toString(Currency.values()));
        }
    }
}