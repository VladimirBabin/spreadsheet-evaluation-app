package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("CONCAT")
public class ConcatFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        for (Input parameter : resolvedParameters) {
            Type type = parameter.getType();
            if (type.equals(Type.ERROR) || type.equals(Type.FORMULA) || type.equals(Type.NOTATION)) {
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }

        String concatenationResult = resolvedParameters.stream()
                .map(Input::getValue)
                .map(Object::toString)
                .map(s -> s.replace("\"", ""))
                .collect(Collectors.joining(""));
        Input cellResult = new Input(concatenationResult);
        return cellResult;
    }
}
