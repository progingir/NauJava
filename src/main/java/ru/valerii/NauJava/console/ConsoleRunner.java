package ru.valerii.NauJava.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.valerii.NauJava.entity.CommandType;

import java.util.Scanner;

@Configuration
public class ConsoleRunner {

    @Bean
    public CommandLineRunner run(CommandProcessor processor) {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {

                while (true) {
                    printHelp();
                    System.out.print("> ");
                    String input = scanner.nextLine().trim().toLowerCase();

                    CommandType command = CommandType.fromString(input);

                    if (command == null) {
                        System.out.println(">> Неизвестная команда '" + input + "'");
                        continue;
                    }

                    if (command == CommandType.EXIT) {
                        System.out.println("Завершение работы...");
                        break;
                    }

                    switch (command) {
                        case ADD -> processor.processAdd(scanner);
                        case LIST -> processor.processList();
                        case TOTAL -> processor.processTotal(scanner);
                        case UPDATE -> processor.processUpdate(scanner);
                        case DELETE -> processor.processDelete(scanner);
                    }
                    System.out.println();
                }
            }
        };
    }

    private void printHelp() {
        System.out.println("Доступные команды:");
        for (CommandType cmd : CommandType.values()) {
            System.out.printf(" %-6s - %s%n", cmd.getCommand(), cmd.getDescription());
        }
    }
}