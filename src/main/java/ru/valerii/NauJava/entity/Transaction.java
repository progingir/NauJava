package ru.valerii.NauJava.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transaction {
    private Long id;
    private BigDecimal amount;
    private Currency currency;
    private String description;
}