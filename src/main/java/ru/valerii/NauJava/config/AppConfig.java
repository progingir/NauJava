package ru.valerii.NauJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.valerii.NauJava.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public List<Transaction> transactionDatabase() {
        return new ArrayList<>();
    }
}