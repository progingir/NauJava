package ru.valerii.NauJava.repository;

import ru.valerii.NauJava.entity.FinancialTransaction;
import java.util.List;

public interface TransactionRepositoryCustom {
    List<FinancialTransaction> findTransactionsByClientEmailCriteria(String email);
}