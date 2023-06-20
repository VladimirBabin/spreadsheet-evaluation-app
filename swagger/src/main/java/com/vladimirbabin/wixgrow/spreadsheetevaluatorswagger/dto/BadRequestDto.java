package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.dto;

public class BadRequestDto {

    private String status;
    private String message;

    public BadRequestDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public BadRequestDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
