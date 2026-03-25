package ru.valerii.NauJava.service;

import ru.valerii.NauJava.dto.AccountDto;
import ru.valerii.NauJava.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface SearchService {
    List<AccountDto> searchAccounts(BigDecimal min, BigDecimal max, String currency);
    List<TransactionDto> findTransactionsByEmail(String email);
}