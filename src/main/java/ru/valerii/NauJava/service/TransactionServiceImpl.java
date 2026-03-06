package ru.valerii.NauJava.service;

import org.springframework.stereotype.Service;
import ru.valerii.NauJava.entity.Currency;
import ru.valerii.NauJava.entity.Transaction;
import ru.valerii.NauJava.exception.TransactionNotFoundException;
import ru.valerii.NauJava.exception.TransactionValidationException;
import ru.valerii.NauJava.repository.CrudRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final CrudRepository<Transaction> repository;

    public TransactionServiceImpl(CrudRepository<Transaction> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Double amount, String currencyCode, String description) {
        validateTransactionData(amount, currencyCode);

        Long generatedId = repository.readAll().stream()
                .mapToLong(Transaction::getId)
                .max()
                .orElse(0L) + 1L;

        Transaction t = new Transaction(generatedId, amount, Currency.valueOf(currencyCode.toUpperCase()), description);
        repository.create(t);
    }

    @Override
    public void update(Long id, Double amount, String currencyCode, String description) {
        Transaction existing = repository.read(id);
        if (existing == null) {
            throw new TransactionNotFoundException("Транзакция с ID " + id + " не найдена.");
        }

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
    public Double calculateTotalAmount(String targetCurrencyCode) {
        Currency targetCurrency = Currency.valueOf(targetCurrencyCode.toUpperCase());

        return repository.readAll().stream()
                .mapToDouble(t -> t.getCurrency().convertTo(targetCurrency, t.getAmount()))
                .sum();
    }

    @Override
    public void remove(Long id) {
        if (repository.read(id) == null) {
            throw new TransactionNotFoundException("Транзакция с ID " + id + " не найдена.");
        }
        repository.delete(id);
    }

    private void validateTransactionData(Double amount, String currencyCode) {
        if (amount <= 0) {
            throw new TransactionValidationException("Сумма транзакции должна быть больше нуля.");
        }
        if (!Currency.isValid(currencyCode)) {
            throw new TransactionValidationException("Неверная валюта! Доступны: RUB, USD, EUR.");
        }
    }
}