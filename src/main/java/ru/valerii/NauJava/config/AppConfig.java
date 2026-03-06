package ru.valerii.NauJava.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.valerii.NauJava.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Bean
    public List<Transaction> transactionDatabase() {
        return new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        System.out.printf("Приложение '%s' (версия: %s) успешно запущено%n", appName, appVersion);
    }
}