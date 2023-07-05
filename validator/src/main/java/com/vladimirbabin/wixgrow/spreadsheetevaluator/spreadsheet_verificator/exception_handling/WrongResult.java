package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.ValidatorResponse;

public class WrongResult extends RuntimeException{
    private final ValidatorResponse validatorResponse;
    public WrongResult(ValidatorResponse validatorResponse) {
        super();
        this.validatorResponse = validatorResponse;
    }

    public ValidatorResponse getValidatorResponse() {
        return validatorResponse;
    }
}
