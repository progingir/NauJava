package ru.valerii.NauJava.config;

import org.springframework.stereotype.Component;
import ru.valerii.NauJava.entity.Currency;
import ru.valerii.NauJava.exception.TransactionNotFoundException;
import ru.valerii.NauJava.exception.TransactionValidationException;
import ru.valerii.NauJava.service.TransactionService;

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
            Double amount = readDouble(scanner, "Введите сумму: ");
            String currency = readCurrency(scanner, "Введите валюту (RUB, USD, EUR): ");

            String description = readString(scanner, "Введите описание: ");

            service.save(amount, currency, description);
            System.out.println(">> Транзакция успешно сохранена! Ей присвоен уникальный ID.");
        } catch (TransactionValidationException e) {
            System.out.println(">> Ошибка валидации: " + e.getMessage());
        }
    }

    public void processUpdate(Scanner scanner) {
        System.out.println("--- Обновление транзакции ---");
        try {
            Long id = readLong(scanner, "Введите ID существующей транзакции: ");
            Double amount = readDouble(scanner, "Введите новую сумму: ");
            String currency = readCurrency(scanner, "Введите новую валюту (RUB, USD, EUR): ");

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

        String targetCurrency = readCurrency(scanner, "В какой валюте вы хотите вывести сумму всех транзакций? (RUB, USD, EUR): ");

        Double total = service.calculateTotalAmount(targetCurrency);
        System.out.printf(">> Общая сумма всех операций: %.2f %s\n", total, targetCurrency);
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

    private Double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println(">> Ошибка: введите корректное число (например, 100.50).");
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
                System.out.println(">> Ошибка: неверная валюта. Доступны только: RUB, USD, EUR.");
            }
        }
    }

    private String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}