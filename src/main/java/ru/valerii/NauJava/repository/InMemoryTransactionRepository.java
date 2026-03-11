package ru.valerii.NauJava.repository;

import org.springframework.stereotype.Repository;
import ru.valerii.NauJava.entity.Transaction;

import java.util.List;

@Repository
public class InMemoryTransactionRepository implements CrudRepository<Transaction> {

    private final List<Transaction> database;

    public InMemoryTransactionRepository(List<Transaction> database) {
        this.database = database;
    }

    @Override
    public void create(Transaction entity) {
        Long generatedId = database.stream()
                .mapToLong(Transaction::getId)
                .max()
                .orElse(0L) + 1L;

        entity.setId(generatedId);

        database.add(entity);
    }

    @Override
    public Transaction read(Long id) {
        return database.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Transaction> readAll() {
        return List.copyOf(database);
    }

    @Override
    public void update(Transaction entity) {
        Transaction existing = read(entity.getId());
        if (existing != null) {
            existing.setAmount(entity.getAmount());
            existing.setCurrency(entity.getCurrency());
            existing.setDescription(entity.getDescription());
        }
    }

    @Override
    public void delete(Long id) {
        database.removeIf(t -> t.getId().equals(id));
    }
}