package com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO;

public class Message {
    private String message;

    @Override
    public String toString() {
        return "message='" + message + '\'';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message() {
    }
}
