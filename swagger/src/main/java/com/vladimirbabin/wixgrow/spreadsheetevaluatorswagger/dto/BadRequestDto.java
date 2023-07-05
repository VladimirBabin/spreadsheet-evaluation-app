package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestDto {

    private HttpStatus status;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String message;
    private List<Report> reports;

    public BadRequestDto() {
    }

    public BadRequestDto(String message, List<Report> reports) {
        this.message = message;
        this.reports = reports;
    }

    public BadRequestDto(String message, HttpStatus status, List<Report> reports) {
        this.message = message;
        this.status = status;
        this.reports = reports;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
