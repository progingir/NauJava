package ru.valerii.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.valerii.NauJava.entity.*;
import ru.valerii.NauJava.repository.*;
import ru.valerii.NauJava.service.ClientService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yaml")
class DatabaseTests {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FinancialTransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;

    @Test
    void testCustomQueriesAndCriteria() {
        Bank bank = new Bank();
        bank.setName("TechBank");
        bank.setSwiftCode("TB123");
        bankRepository.save(bank);

        Client client = new Client();
        client.setFullName("Ivan Ivanov");
        client.setEmail("ivan@test.com");
        clientRepository.save(client);

        Account acc = new Account();
        acc.setAccountNumber("12345");
        acc.setBalance(5000.0);
        acc.setCurrency("RUB");
        acc.setBank(bank);
        acc.setClient(client);
        accountRepository.save(acc);

        FinancialTransaction tx = new FinancialTransaction();
        tx.setAmount(1000.0);
        tx.setOperationDate(LocalDateTime.now());
        tx.setAccount(acc);
        transactionRepository.save(tx);

        List<Account> accountsDataJpa = accountRepository.findByBalanceBetweenAndCurrency(1000.0, 6000.0, "RUB");
        Assertions.assertEquals(1, accountsDataJpa.size());

        List<Account> accountsCriteria = accountRepository.searchAccountsByCriteria(1000.0, 6000.0, "RUB");
        Assertions.assertEquals(1, accountsCriteria.size());

        List<FinancialTransaction> txDataJpa = transactionRepository.findByClientEmail("ivan@test.com");
        Assertions.assertEquals(1, txDataJpa.size());

        List<FinancialTransaction> txCriteria = transactionRepository.findTransactionsByClientEmailCriteria("ivan@test.com");
        Assertions.assertEquals(1, txCriteria.size());
    }

    @Test
    void testTransactionRollback() {
        Client client = new Client();
        client.setFullName("Rollback Tester");
        client.setEmail("rollback@test.com");
        clientRepository.save(client);
        Long clientId = client.getId();

        Assertions.assertThrows(RuntimeException.class, () -> {
            clientService.deleteClientAndAccounts(clientId, true);
        });

        Assertions.assertTrue(clientRepository.findById(clientId).isPresent());
    }

    @Test
    void testTransactionSuccess() {
        Client client = new Client();
        client.setFullName("Success Tester");
        client.setEmail("success@test.com");
        clientRepository.save(client);
        Long clientId = client.getId();

        clientService.deleteClientAndAccounts(clientId, false);

        Assertions.assertTrue(clientRepository.findById(clientId).isEmpty());
    }
}