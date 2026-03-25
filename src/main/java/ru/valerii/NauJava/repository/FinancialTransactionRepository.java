package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.valerii.NauJava.entity.FinancialTransaction;

import java.util.List;

@RepositoryRestResource(path = "transactions")
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long>, TransactionRepositoryCustom {
    @Query("SELECT t FROM FinancialTransaction t WHERE t.account.client.email = :email")
    List<FinancialTransaction> findByClientEmail(@Param("email") String email);
}