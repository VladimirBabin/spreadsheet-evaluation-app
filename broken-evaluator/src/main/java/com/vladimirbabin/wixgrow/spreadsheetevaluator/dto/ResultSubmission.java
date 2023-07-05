package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.AppProperties;

import java.util.List;

@SuppressWarnings("rawtypes")
//Broken evaluator doesn't have a perfect style
public class ResultSubmission {
    private final String email;
    private List<Sheet> results;

    public ResultSubmission(AppProperties properties) {
        email = properties.getEmailForResultSubmission();
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
