package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

import java.util.List;

public class Spreadsheet {
    private String submissionUrl;
    private List<Sheet<Object>> sheets;

    public Spreadsheet() {
    }

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }

    public List<Sheet<Object>> getSheets() {
        return sheets;
    }

    public void setSheets(List<Sheet<Object>> sheets) {
        this.sheets = sheets;
    }

    @Override
    public String toString() {
        return "Spreadsheet{" +
                "submissionUrl='" + submissionUrl + '\'' +
                ", sheets=" + sheets +
                '}';
    }
}
