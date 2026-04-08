package ru.valerii.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valerii.NauJava.entity.Report;
import ru.valerii.NauJava.entity.User;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByOwner(User owner);
}