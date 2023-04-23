package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("OR")
public class OrFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        for (Input parameter : resolvedParameters) {
            if (!parameter.getType().equals(Type.BOOLEAN)) {
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }
        Optional<Boolean> optional = resolvedParameters.stream()
                .map(i -> i.getValue().toString())
                .map(Boolean::parseBoolean)
                .reduce(Boolean::logicalOr);

        Boolean resultOfAndFormula;
        if (optional.isPresent()) {
            resultOfAndFormula = optional.get();
        } else {
            Input errorCell = new Input("#ERROR: The OR formula result can't be null");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        Input cellResult = new Input(resultOfAndFormula);
        return cellResult;
    }
}