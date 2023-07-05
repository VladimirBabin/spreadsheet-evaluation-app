package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.exception_handling;

import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.dto.BadRequestDto;

public class WrongResponseException extends RuntimeException {

    private final BadRequestDto badRequestDto;

    public WrongResponseException(BadRequestDto badRequestDto) {
        super();
        this.badRequestDto = badRequestDto;
    }

    public BadRequestDto getBadRequestDto() {
        return badRequestDto;
    }

    @Override
    public String toString() {
        return "WrongResponseException { " +
                badRequestDto +
                " }";
    }
}
