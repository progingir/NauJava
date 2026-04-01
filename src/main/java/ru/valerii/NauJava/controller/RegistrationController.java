package ru.valerii.NauJava.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.valerii.NauJava.dto.UserRegistrationDto;
import ru.valerii.NauJava.exception.UserAlreadyExistsException;
import ru.valerii.NauJava.service.UserService;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {

        model.addAttribute("userDto", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userService.registerUser(userDto);
            return "redirect:/login";
        } catch (UserAlreadyExistsException ex) {
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}