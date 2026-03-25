package ru.valerii.NauJava.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Currency {
    RUB(new BigDecimal("1.0")),
    USD(new BigDecimal("100.0")),
    EUR(new BigDecimal("110.0"));

    private final BigDecimal rateToBase;

    Currency(BigDecimal rateToBase) {
        this.rateToBase = rateToBase;
    }

    public BigDecimal convertTo(Currency targetCurrency, BigDecimal amount) {
        BigDecimal amountInBase = amount.multiply(this.rateToBase);

        return amountInBase.divide(targetCurrency.rateToBase, 2, RoundingMode.HALF_UP);
    }

    public static boolean isValid(String code) {
        for (Currency c : values()) {
            if (c.name().equalsIgnoreCase(code)) return true;
        }
        return false;
    }
}