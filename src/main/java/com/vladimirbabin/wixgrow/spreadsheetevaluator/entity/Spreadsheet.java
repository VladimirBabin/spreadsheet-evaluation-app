package com.vladimirbabin.wixgrow.spreadsheetevaluator.entity;

import java.util.List;

public class Spreadsheet {
    private String submissionUrl;
    private List<Sheet> sheets;

    public Spreadsheet() {
    }

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }

    public List<Sheet> getSheets() {
        return sheets;
    }

    public void setSheets(List<Sheet> sheets) {
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
