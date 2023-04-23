package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.springframework.stereotype.Component;

@Component("NOTATION")
public class NotationApplier implements FormulaApplier {
    InputTypeDeterminer inputTypeDeterminer;

    public NotationApplier(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Input apply(String formulaContents, Sheet<Input> sheet) {
        Object resultObject = sheet.getElementByNotation(formulaContents);
        Input resultInput = inputTypeDeterminer.determineType(new Input(resultObject));
        return resultInput;
    }
}
