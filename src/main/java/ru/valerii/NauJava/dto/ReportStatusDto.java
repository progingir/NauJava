package ru.valerii.NauJava.dto;

import ru.valerii.NauJava.entity.ReportStatus;

public class ReportStatusDto {
    private String status;

    public ReportStatusDto(ReportStatus status) {
        this.status = status.name();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}