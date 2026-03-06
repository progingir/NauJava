package ru.valerii.NauJava.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Double amount;
    private Currency currency;
    private String description;
}