package ru.valerii.NauJava.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.valerii.NauJava.entity.Report;
import ru.valerii.NauJava.entity.ReportStatus;
import ru.valerii.NauJava.entity.User;
import ru.valerii.NauJava.entity.Client;
import ru.valerii.NauJava.repository.ClientRepository;
import ru.valerii.NauJava.repository.ReportRepository;
import ru.valerii.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final TemplateEngine templateEngine;

    public ReportService(ReportRepository reportRepository,
                         UserRepository userRepository,
                         ClientRepository clientRepository,
                         TemplateEngine templateEngine) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.templateEngine = templateEngine;
    }

    @Transactional
    public Long createReport(User owner) {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        report.setOwner(owner);
        report.setName("Отчет от " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM HH:mm:ss")));
        report.setCreatedAt(LocalDateTime.now());
        return reportRepository.save(report).getId();
    }

    @Transactional(readOnly = true)
    public Report getReportForUser(Long id, String username) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отчет не найден"));

        if (!report.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Доступ запрещен");
        }
        return report;
    }

    @Transactional(readOnly = true)
    public List<Report> getReportsByUser(User owner) {
        return reportRepository.findAllByOwner(owner);
    }

    public void generateReportAsync(Long reportId) {
        CompletableFuture.runAsync(() -> {
            long totalStartTime = System.currentTimeMillis();

            try {
                AtomicLong userCount = new AtomicLong(0);
                AtomicLong userTime = new AtomicLong(0);
                List<Client> clients = new java.util.ArrayList<>();
                AtomicLong clientTime = new AtomicLong(0);

                Thread userThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    userCount.set(userRepository.count());
                    userTime.set(System.currentTimeMillis() - start);
                });

                Thread clientThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    List<Client> allClients = clientRepository.findAll();
                    clients.addAll(allClients);
                    clientTime.set(System.currentTimeMillis() - start);
                });

                userThread.start();
                clientThread.start();

                userThread.join();
                clientThread.join();

                long totalTime = System.currentTimeMillis() - totalStartTime;

                Context context = new Context();
                context.setVariable("userCount", userCount.get());
                context.setVariable("userTime", userTime.get());
                context.setVariable("clients", clients);
                context.setVariable("clientTime", clientTime.get());
                context.setVariable("totalTime", totalTime);

                String html = templateEngine.process("report-template", context);

                updateReportStatus(reportId, html, ReportStatus.COMPLETED);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                updateReportStatus(reportId, "Формирование прервано: " + e.getMessage(), ReportStatus.ERROR);
            } catch (Exception e) {
                updateReportStatus(reportId, "Ошибка: " + e.getMessage(), ReportStatus.ERROR);
            }
        });
    }

    @Transactional
    public void updateReportStatus(Long id, String content, ReportStatus status) {
        reportRepository.findById(id).ifPresent(r -> {
            r.setContent(content);
            r.setStatus(status);
            reportRepository.save(r);
        });
    }
}