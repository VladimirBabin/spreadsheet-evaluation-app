package com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.ConfigurationProperties;

import java.util.List;

public class ResultSubmission {
    private final String email;
    private List<Sheet> results;

    public ResultSubmission(ConfigurationProperties properties) {
        email = properties.getEmailForResulSubmission();
    }

    public String getEmail() {
        return email;
    }

    public List<Sheet> getResults() {
        return results;
    }

    public void setResults(List<Sheet> results) {
        this.results = results;
    }
}
