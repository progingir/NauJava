package ru.valerii.NauJava;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.valerii.NauJava.entity.Account;
import ru.valerii.NauJava.entity.FinancialTransaction;
import ru.valerii.NauJava.exception.TransactionValidationException;
import ru.valerii.NauJava.repository.AccountRepository;
import ru.valerii.NauJava.repository.FinancialTransactionRepository;
import ru.valerii.NauJava.service.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldReturnTransactionIdWhenSavedSuccessfully() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Тестовый перевод";
        Account account = new Account();
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        when(transactionRepository.save(any(FinancialTransaction.class))).thenAnswer(invocation -> {
            FinancialTransaction ft = invocation.getArgument(0);
            ft.setId(10L);
            return ft;
        });

        Long resultId = transactionService.save(accountId, amount, description);

        assertNotNull(resultId);
        assertEquals(10L, resultId);
        verify(accountRepository).findById(accountId);

        ArgumentCaptor<FinancialTransaction> captor = ArgumentCaptor.forClass(FinancialTransaction.class);
        verify(transactionRepository).save(captor.capture());

        FinancialTransaction capturedTransaction = captor.getValue();
        assertEquals(amount, capturedTransaction.getAmount());
        assertEquals(description, capturedTransaction.getDescription());
        assertEquals(account, capturedTransaction.getAccount());
        assertEquals("COMPLETED", capturedTransaction.getStatus());
        assertNotNull(capturedTransaction.getOperationDate());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZeroOrLess() {
        Exception exception = assertThrows(TransactionValidationException.class, () -> {
            transactionService.save(1L, BigDecimal.ZERO, "Тест");
        });

        assertEquals("Сумма должна быть больше нуля", exception.getMessage());
        verifyNoInteractions(accountRepository, transactionRepository);
    }

    @Test
    void shouldThrowExceptionWhenAccountIsNotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TransactionValidationException.class, () -> {
            transactionService.save(99L, new BigDecimal("50.00"), "Тест");
        });

        assertTrue(exception.getMessage().contains("не найден"));
        verify(accountRepository).findById(99L);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldCalculateTotalAmountCorrectly() {
        String targetCurrency = "RUB";

        Account account = new Account();
        account.setCurrency("RUB");
        FinancialTransaction tx1 = new FinancialTransaction();
        tx1.setAmount(new BigDecimal("150.50"));
        tx1.setAccount(account);

        FinancialTransaction tx2 = new FinancialTransaction();
        tx2.setAmount(new BigDecimal("50.25"));
        tx2.setAccount(account);

        when(transactionRepository.findAll()).thenReturn(List.of(tx1, tx2));

        BigDecimal totalAmount = transactionService.calculateTotalAmount(targetCurrency);

        assertEquals(new BigDecimal("200.75"), totalAmount);

        verify(transactionRepository, times(1)).findAll();
    }
}