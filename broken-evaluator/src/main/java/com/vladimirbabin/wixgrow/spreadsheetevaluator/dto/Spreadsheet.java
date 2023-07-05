package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import java.util.List;


public class Spreadsheet {
    private String submissionUrl;
    @SuppressWarnings("rawtypes")
    //Broken evaluator doesn't have a perfect style
    private List<Sheet> sheets;

    public Spreadsheet() {
    }

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }
    @SuppressWarnings("rawtypes")
    //Broken evaluator doesn't have a perfect style
    public List<Sheet> getSheets() {
        return sheets;
    }

    @SuppressWarnings("rawtypes")
    //Broken evaluator doesn't have a perfect style
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
