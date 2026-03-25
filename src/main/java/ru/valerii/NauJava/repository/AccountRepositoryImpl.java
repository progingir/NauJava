package ru.valerii.NauJava.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.valerii.NauJava.entity.Account;
import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Account> searchAccountsByCriteria(Double min, Double max, String currency) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);

        Predicate balancePredicate = cb.between(root.get("balance"), min, max);
        Predicate currencyPredicate = cb.equal(root.get("currency"), currency);

        cq.select(root).where(cb.and(balancePredicate, currencyPredicate));
        return entityManager.createQuery(cq).getResultList();
    }
}