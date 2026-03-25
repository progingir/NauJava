package ru.valerii.NauJava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valerii.NauJava.dto.AccountDto;
import ru.valerii.NauJava.dto.TransactionDto;
import ru.valerii.NauJava.mapper.EntityMapper;
import ru.valerii.NauJava.repository.AccountRepository;
import ru.valerii.NauJava.repository.FinancialTransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final EntityMapper entityMapper;

    public SearchServiceImpl(AccountRepository accountRepository,
                             FinancialTransactionRepository transactionRepository,
                             EntityMapper entityMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> searchAccounts(BigDecimal min, BigDecimal max, String currency) {
        return accountRepository.searchAccountsByCriteria(min, max, currency)
                .stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> findTransactionsByEmail(String email) {
        return transactionRepository.findByClientEmail(email)
                .stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());
    }
}