package ru.valerii.NauJava.repository;

import org.springframework.stereotype.Repository;
import ru.valerii.NauJava.entity.Transaction;

import java.util.List;
import java.util.Optional;

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
    public Optional<Transaction> read(Long id) {
        return database.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Transaction> readAll() {
        return List.copyOf(database);
    }

    @Override
    public void update(Transaction entity) {
        read(entity.getId()).ifPresent(existing -> {
            existing.setAmount(entity.getAmount());
            existing.setCurrency(entity.getCurrency());
            existing.setDescription(entity.getDescription());
        });
    }

    @Override
    public void delete(Long id) {
        database.removeIf(t -> t.getId().equals(id));
    }
}