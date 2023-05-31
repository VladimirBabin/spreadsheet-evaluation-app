package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling;

public class WrongResult extends RuntimeException{
    public WrongResult(String message) {
        super(message);
    }
}
