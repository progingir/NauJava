package ru.valerii.NauJava.entity;

public enum Currency {
    RUB(1.0),
    USD(100.0),
    EUR(110.0);

    private final double rateToBase;

    Currency(double rateToBase) {
        this.rateToBase = rateToBase;
    }

    public double convertTo(Currency targetCurrency, double amount) {
        double amountInBase = amount * this.rateToBase;
        return amountInBase / targetCurrency.rateToBase;
    }

    public static boolean isValid(String code) {
        for (Currency c : values()) {
            if (c.name().equalsIgnoreCase(code)) return true;
        }
        return false;
    }
}