package com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO;

import java.util.List;

public class ResultSubmission {
    private final String email = "vladimir.sol.rojo@gmail.com";
    private List<Sheet> results;

    public ResultSubmission() {
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