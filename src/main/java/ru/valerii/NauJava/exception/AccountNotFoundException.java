package ru.valerii.NauJava.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Счет с ID " + id + " не найден");
    }
}