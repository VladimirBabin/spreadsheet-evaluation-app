package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

import java.util.List;

public class SpreadsheetToVerify {
    private String email;
    private List<Sheet> results;

    public SpreadsheetToVerify() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Sheet> getResults() {
        return results;
    }

    public void setResults(List<Sheet> results) {
        this.results = results;
    }
}
