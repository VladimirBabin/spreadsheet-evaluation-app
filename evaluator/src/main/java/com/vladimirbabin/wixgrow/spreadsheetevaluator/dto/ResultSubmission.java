package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.AppProperties;

import java.util.List;

public class ResultSubmission {
    private final String email;
    private List<Sheet<Object>> results;

    public ResultSubmission(AppProperties properties) {
        email = properties.getEmailForResultSubmission();
    }

    public String getEmail() {
        return email;
    }

    public List<Sheet<Object>> getResults() {
        return results;
    }

    public void setResults(List<Sheet<Object>> results) {
        this.results = results;
    }
}
