package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("CONCAT")
public class ConcatFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        String concatenationResult = resolvedParameters.stream()
                .map(Input::getValue)
                .map(Object::toString)
                .map(s -> s.replace("\"", ""))
                .collect(Collectors.joining(""));
        Input cellResult = new Input(concatenationResult);
        return cellResult;
    }

    @Override
    public Input apply(Input singleParameter, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with multiple Input parameters");
    }

    @Override
    public Input apply(String formulaContents, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with multiple Input parameters");
    }
}
