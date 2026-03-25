package ru.valerii.NauJava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valerii.NauJava.entity.Account;
import ru.valerii.NauJava.entity.Currency;
import ru.valerii.NauJava.entity.FinancialTransaction;
import ru.valerii.NauJava.exception.TransactionNotFoundException;
import ru.valerii.NauJava.exception.TransactionValidationException;
import ru.valerii.NauJava.repository.AccountRepository;
import ru.valerii.NauJava.repository.FinancialTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(FinancialTransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Long save(Long accountId, BigDecimal amount, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionValidationException("Сумма должна быть больше нуля");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new TransactionValidationException("Счет с ID " + accountId + " не найден"));

        FinancialTransaction ft = new FinancialTransaction();
        ft.setAmount(amount);
        ft.setDescription(description);
        ft.setAccount(account);
        ft.setOperationDate(LocalDateTime.now());
        ft.setStatus("COMPLETED");

        transactionRepository.save(ft);
        return ft.getId();
    }

    @Override
    @Transactional
    public void update(Long id, BigDecimal amount, String description) {
        FinancialTransaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция не найдена"));

        existing.setAmount(amount);
        existing.setDescription(description);
        transactionRepository.save(existing);
    }

    @Override
    public List<FinancialTransaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAmount(String targetCurrencyCode) {
        Currency targetCurrency = Currency.valueOf(targetCurrencyCode.toUpperCase());

        return transactionRepository.findAll().stream()
                .map(t -> {
                    Currency sourceCurrency = Currency.valueOf(t.getAccount().getCurrency().toUpperCase());
                    return sourceCurrency.convertTo(targetCurrency, t.getAmount());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException("Транзакция не найдена");
        }
        transactionRepository.deleteById(id);
    }
}