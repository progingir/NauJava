package ru.valerii.NauJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.valerii.NauJava.repository.ClientRepository;

@Controller
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "clients";
    }
}