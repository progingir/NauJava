package ru.valerii.NauJava.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.valerii.NauJava.entity.Account;
import ru.valerii.NauJava.entity.Client;
import ru.valerii.NauJava.entity.FinancialTransaction;

import java.util.List;

@Repository
public class FinancialTransactionRepositoryImpl implements TransactionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinancialTransaction> findTransactionsByClientEmailCriteria(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FinancialTransaction> cq = cb.createQuery(FinancialTransaction.class);
        Root<FinancialTransaction> root = cq.from(FinancialTransaction.class);

        Join<FinancialTransaction, Account> accountJoin = root.join("account", JoinType.INNER);
        Join<Account, Client> clientJoin = accountJoin.join("client", JoinType.INNER);

        Predicate emailPredicate = cb.equal(clientJoin.get("email"), email);
        cq.select(root).where(emailPredicate);

        return entityManager.createQuery(cq).getResultList();
    }
}