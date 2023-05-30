package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

import java.util.List;

public class SpreadsheetToVerify {
    private String email;
    private List<Sheet> sheets;

    public SpreadsheetToVerify() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Sheet> getSheets() {
        return sheets;
    }

    public void setSheets(List<Sheet> results) {
        this.sheets = results;
    }
}
