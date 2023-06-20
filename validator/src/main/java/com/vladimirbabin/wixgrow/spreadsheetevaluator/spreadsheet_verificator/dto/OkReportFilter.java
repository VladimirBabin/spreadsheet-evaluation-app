package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto;

public class OkReportFilter {

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Boolean)) {
            return false;
        }
        boolean object = (boolean) obj;
        if (object) return false;
        return true;
    }
}
