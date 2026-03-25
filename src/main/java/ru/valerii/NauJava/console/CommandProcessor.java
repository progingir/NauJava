package ru.valerii.NauJava.console;

import org.springframework.stereotype.Component;
import ru.valerii.NauJava.entity.Currency;
import ru.valerii.NauJava.exception.TransactionNotFoundException;
import ru.valerii.NauJava.exception.TransactionValidationException;
import ru.valerii.NauJava.service.TransactionService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

@Component
public class CommandProcessor {

    private final TransactionService service;

    public CommandProcessor(TransactionService service) {
        this.service = service;
    }

    public void processAdd(Scanner scanner) {
        System.out.println("--- Регистрация новой транзакции ---");
        try {
            BigDecimal amount = readBigDecimal(scanner, "Введите сумму: ");
            String currency = readCurrency(scanner, "Введите валюту " + Arrays.toString(Currency.values()) + ": ");
            String description = readString(scanner, "Введите описание: ");

            Long id = service.save(amount, currency, description);
            System.out.println(">> Транзакция успешно сохранена! Ей присвоен ID: " + id);
        } catch (TransactionValidationException e) {
            System.out.println(">> Ошибка валидации: " + e.getMessage());
        }
    }

    public void processUpdate(Scanner scanner) {
        System.out.println("--- Обновление транзакции ---");
        try {
            Long id = readLong(scanner, "Введите ID существующей транзакции: ");
            BigDecimal amount = readBigDecimal(scanner, "Введите новую сумму: ");
            String currency = readCurrency(scanner, "Введите новую валюту " + Arrays.toString(Currency.values()) + ": ");
            String description = readString(scanner, "Введите новое описание: ");

            service.update(id, amount, currency, description);
            System.out.println(">> Транзакция успешно обновлена!");
        } catch (TransactionNotFoundException | TransactionValidationException e) {
            System.out.println(">> Ошибка: " + e.getMessage());
        }
    }

    public void processList() {
        System.out.println("--- Список всех транзакций ---");
        var list = service.getAll();
        if (list.isEmpty()) {
            System.out.println("Список пока пуст.");
        } else {
            list.forEach(System.out::println);
        }
    }

    public void processTotal(Scanner scanner) {
        System.out.println("--- Подсчет общей суммы ---");
        String targetCurrency = readCurrency(scanner, "В какой валюте вывести сумму? " + Arrays.toString(Currency.values()) + ": ");
        BigDecimal total = service.calculateTotalAmount(targetCurrency);
        System.out.printf(">> Общая сумма всех операций: %s %s%n", total.toPlainString(), targetCurrency);
    }

    public void processDelete(Scanner scanner) {
        Long id = readLong(scanner, "Введите ID транзакции для удаления: ");
        try {
            service.remove(id);
            System.out.println(">> Транзакция удалена");
        } catch (TransactionNotFoundException e) {
            System.out.println(">> Ошибка: " + e.getMessage());
        }
    }

    private BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim().replace(",", ".");
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println(">> Ошибка: введите число (например, 100.50).");
            }
        }
    }

    private Long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Ошибка: введите целое число.");
            }
        }
    }

    private String readCurrency(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String currencyCode = scanner.nextLine().trim().toUpperCase();
            if (Currency.isValid(currencyCode)) {
                return currencyCode;
            } else {
                System.out.println(">> Ошибка! Доступны только: " + Arrays.toString(Currency.values()));
            }
        }
    }

    private String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}