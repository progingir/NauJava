package ru.valerii.NauJava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.valerii.NauJava.dto.ReportIdDto;
import ru.valerii.NauJava.dto.ReportStatusDto;
import ru.valerii.NauJava.entity.Report;
import ru.valerii.NauJava.entity.User;
import ru.valerii.NauJava.repository.UserRepository;
import ru.valerii.NauJava.service.ReportService;

import java.security.Principal;

@RestController
@RequestMapping("/api/reports")
public class ReportRestController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    public ReportRestController(ReportService reportService, UserRepository userRepository) {
        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<ReportIdDto> startGeneration(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Long id = reportService.createReport(user);

        reportService.generateReportAsync(id);

        return ResponseEntity.ok(new ReportIdDto(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ReportStatusDto> getStatus(@PathVariable Long id, Principal principal) {
        Report report = reportService.getReportForUser(id, principal.getName());

        return ResponseEntity.ok(new ReportStatusDto(report.getStatus()));
    }
}