package com.vladimirbabin.wixgrow.spreadsheetevaluator.exception_handling;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.BadRequestDto;

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
