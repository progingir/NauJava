package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.valerii.NauJava.entity.FinancialTransaction;

import java.util.List;

public interface FinancialTransactionRepository extends CrudRepository<FinancialTransaction, Long>, TransactionRepositoryCustom {
    @Query("SELECT t FROM FinancialTransaction t WHERE t.account.client.email = :email")
    List<FinancialTransaction> findByClientEmail(@Param("email") String email);
}