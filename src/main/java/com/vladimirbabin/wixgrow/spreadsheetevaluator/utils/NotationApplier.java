package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("NOTATION")
public class NotationApplier implements FormulaApplier {
    InputTypeDeterminer inputTypeDeterminer;

    public NotationApplier(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Input apply(List<Input> parameters, Sheet<Input> sheet) {
        Input singleParameter = parameters.get(0);
        Object resultObject = sheet.getElementByNotation(singleParameter.getValue().toString());
        Input resultInput = inputTypeDeterminer.determineType(new Input(resultObject));
        return resultInput;
    }


    public Input apply(String notation, Sheet<Input> sheet) {
        Object resultObject = sheet.getElementByNotation(notation);
        Input resultInput = inputTypeDeterminer.determineType(new Input(resultObject));
        return resultInput;
    }


}
