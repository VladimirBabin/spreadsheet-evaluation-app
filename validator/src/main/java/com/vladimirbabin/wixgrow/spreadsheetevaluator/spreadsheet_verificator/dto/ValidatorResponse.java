package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ValidatorResponse {
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private List<Report> reports;

    public ValidatorResponse() {
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
