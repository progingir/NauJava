package ru.valerii.NauJava.dto;

public class ReportIdDto {
    private Long reportId;

    public ReportIdDto(Long reportId) {
        this.reportId = reportId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}