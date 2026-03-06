package ru.valerii.NauJava.service;

import org.springframework.stereotype.Service;
import ru.valerii.NauJava.entity.Transaction;
import ru.valerii.NauJava.repository.CrudRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final CrudRepository<Transaction> repository;

    public TransactionServiceImpl(CrudRepository<Transaction> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Long id, Double amount, String currency, String description) {
        Transaction transaction = new Transaction(id, amount, currency, description);
        repository.create(transaction);
    }

    @Override
    public List<Transaction> getAll() {
        return repository.readAll();
    }

    @Override
    public Double calculateTotalAmount() {
        return repository.readAll().stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public void remove(Long id) {
        repository.delete(id);
    }
}