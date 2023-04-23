package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.springframework.stereotype.Component;

@Component("NOTATION")
public class NotationApplier implements FormulaApplier {
    InputTypeDeterminer inputTypeDeterminer;

    public NotationApplier(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Input apply(String formulaContents, Sheet<Input> sheet) {
        if (!formulaContents.matches("[A-Z][1-9]+")) {
            Input errorCell = new Input("#ERROR: Invalid parameter type");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        Object resultObject = sheet.getElementByNotation(formulaContents);
        Input resultInput = inputTypeDeterminer.determineType(new Input(resultObject));
        return resultInput;
    }
}
