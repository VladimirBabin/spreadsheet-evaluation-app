package com.vladimirbabin.wixgrow.spreadsheetevaluator.exception_handling;

public class WrongResponseException extends RuntimeException {

    public WrongResponseException(String message) {
        super(message);
    }
}
