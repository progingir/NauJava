package ru.valerii.NauJava.mapper;

import org.springframework.stereotype.Component;
import ru.valerii.NauJava.dto.AccountDto;
import ru.valerii.NauJava.dto.ClientDto;
import ru.valerii.NauJava.dto.TransactionDto;
import ru.valerii.NauJava.dto.UserRegistrationDto;
import ru.valerii.NauJava.entity.Account;
import ru.valerii.NauJava.entity.Client;
import ru.valerii.NauJava.entity.FinancialTransaction;
import ru.valerii.NauJava.entity.User;

@Component
public class EntityMapper {

    public AccountDto toDto(Account account) {
        if (account == null) return null;

        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());

        if (account.getClient() != null) {
            dto.setClientName(account.getClient().getFullName());
        }
        return dto;
    }

    public TransactionDto toDto(FinancialTransaction tx) {
        if (tx == null) return null;

        TransactionDto dto = new TransactionDto();
        dto.setId(tx.getId());
        dto.setAmount(tx.getAmount());
        dto.setDescription(tx.getDescription());
        dto.setOperationDate(tx.getOperationDate());
        dto.setStatus(tx.getStatus());
        return dto;
    }

    public ClientDto toDto(Client client) {
        if (client == null) return null;

        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFullName(client.getFullName());
        dto.setEmail(client.getEmail());
        dto.setStatus(client.getStatus());
        return dto;
    }

    public User toEntity(UserRegistrationDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}