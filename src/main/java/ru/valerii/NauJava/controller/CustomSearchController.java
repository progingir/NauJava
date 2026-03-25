package ru.valerii.NauJava.controller;

import org.springframework.web.bind.annotation.*;
import ru.valerii.NauJava.dto.AccountDto;
import ru.valerii.NauJava.dto.TransactionDto;
import ru.valerii.NauJava.service.SearchService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/custom")
public class CustomSearchController {

    private final SearchService searchService;

    public CustomSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/accounts/search")
    public List<AccountDto> searchAccounts(@RequestParam BigDecimal min,
                                           @RequestParam BigDecimal max,
                                           @RequestParam String currency) {
        return searchService.searchAccounts(min, max, currency);
    }

    @GetMapping("/transactions/by-email")
    public List<TransactionDto> getByEmail(@RequestParam String email) {
        return searchService.findTransactionsByEmail(email);
    }
}