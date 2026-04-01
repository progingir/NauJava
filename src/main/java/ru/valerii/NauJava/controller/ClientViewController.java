package ru.valerii.NauJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.valerii.NauJava.dto.ClientDto;
import ru.valerii.NauJava.mapper.EntityMapper;
import ru.valerii.NauJava.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientViewController {

    private final ClientRepository clientRepository;
    private final EntityMapper entityMapper;

    public ClientViewController(ClientRepository clientRepository, EntityMapper entityMapper) {
        this.clientRepository = clientRepository;
        this.entityMapper = entityMapper;
    }

    @GetMapping("/view/clients")
    public String listClients(Model model) {
        List<ClientDto> clients = clientRepository.findAll().stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("clients", clients);
        return "client-list";
    }
}