package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("NOTATION")
public class NotationApplier implements FormulaApplier {
    InputTypeDeterminer inputTypeDeterminer;

    public NotationApplier(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Input apply(List<Input> parameters, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with String parameter");
    }

    @Override
    public Input apply(Input singleParameter, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with String parameter");
    }

    public Input apply(String formulaContents, Sheet<Input> sheet) {
        Object resultObject = sheet.getElementByNotation(formulaContents);
        Input resultInput = inputTypeDeterminer.determineType(new Input(resultObject));
        return resultInput;
    }
}
