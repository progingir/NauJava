package ru.valerii.NauJava.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppInfoPrinter {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @PostConstruct
    public void printInfo() {
        System.out.printf("Приложение '%s' (версия: %s) успешно запущено%n", appName, appVersion);
    }
}