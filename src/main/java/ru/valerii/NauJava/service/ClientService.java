package ru.valerii.NauJava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valerii.NauJava.entity.Client;
import ru.valerii.NauJava.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void deleteClientAndAccounts(Long clientId, boolean simulateError) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        if (simulateError) {
            throw new RuntimeException("Имитация ошибки для отката транзакции");
        }

        clientRepository.delete(client);
    }
}