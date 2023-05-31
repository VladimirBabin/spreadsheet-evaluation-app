package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.exception_handling;

public class WrongResponseException extends RuntimeException {

    public WrongResponseException(String message) {
        super(message);
    }
}
