package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

import java.util.List;

public class SpreadsheetToVerify {
    private String email;
    private List<Sheet<Object>> results;

    public SpreadsheetToVerify() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Sheet<Object>> getResults() {
        return results;
    }

    public void setResults(List<Sheet<Object>> results) {
        this.results = results;
    }
}
