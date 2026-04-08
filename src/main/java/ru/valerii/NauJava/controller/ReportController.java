package ru.valerii.NauJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.valerii.NauJava.entity.Report;
import ru.valerii.NauJava.entity.ReportStatus;
import ru.valerii.NauJava.entity.User;
import ru.valerii.NauJava.repository.UserRepository;
import ru.valerii.NauJava.service.ReportService;

import java.security.Principal;

@Controller
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;

    public ReportController(ReportService reportService, UserRepository userRepository) {
        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showReportPanel(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("reports", reportService.getReportsByUser(user));
        return "report-panel";
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable Long id, Principal principal, Model model) {
        Report report = reportService.getReportForUser(id, principal.getName());

        if (report.getStatus() == ReportStatus.COMPLETED) {
            model.addAttribute("content", report.getContent());
            return "report-final-view";
        }

        model.addAttribute("reportId", id);
        model.addAttribute("statusInfo", report.getStatus() == ReportStatus.ERROR ? "Ошибка" : "Загрузка...");
        return "report-loading";
    }

    @GetMapping("/create")
    public String showCreatePage() {
        return "report-create";
    }

    @PostMapping("/generate")
    public String handleGenerateReport(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        Long id = reportService.createReport(user);

        reportService.generateReportAsync(id);

        return "redirect:/reports/view/" + id;
    }
}