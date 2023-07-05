package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Report {
    private String id;

    @JsonInclude(JsonInclude.Include. NON_NULL)
    private boolean ok;

    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String error;

    public Report() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", ok=" + ok +
                ", error='" + error + '\'' +
                '}';
    }
}
