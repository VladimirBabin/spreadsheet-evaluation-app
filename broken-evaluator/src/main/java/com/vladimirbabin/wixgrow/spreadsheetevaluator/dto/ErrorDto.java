package com.vladimirbabin.wixgrow.spreadsheetevaluator.dto;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ErrorDto {
    private HttpStatus status;
    private String message;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorDto(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public ErrorDto(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
    }
}
