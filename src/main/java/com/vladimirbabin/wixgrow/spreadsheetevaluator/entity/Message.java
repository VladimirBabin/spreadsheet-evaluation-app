package com.vladimirbabin.wixgrow.spreadsheetevaluator.entity;

public class Message {
    private String message;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
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
