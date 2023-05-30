package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

import java.util.List;

public class VerificationResponse {
    private String message;
    private List<Report> reports;

    public VerificationResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "VerificationResponse{" +
                "message='" + message + '\'' +
                ", reports=" + reports +
                '}';
    }
}
