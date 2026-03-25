package ru.valerii.NauJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.valerii.NauJava.repository.ClientRepository;

@Controller
public class ClientViewController {
    private final ClientRepository clientRepository;

    public ClientViewController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/view/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "client-list";
    }
}