package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("AND")
public class AndFormulaApplier implements FormulaApplier {
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
                .reduce(Boolean::logicalAnd);

        Boolean resultOfAndFormula;
        if (optional.isPresent()) {
            resultOfAndFormula = optional.get();
        } else {
            throw new RuntimeException("The And formula result can't be null");
        }

        Input cellResult = new Input(resultOfAndFormula);
        cellResult.setType(Type.BOOLEAN);
        return cellResult;
    }

    @Override
    public Input apply(Input singleParameter, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with multiple Input parameter");
    }

    @Override
    public Input apply(String formulaContents, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with multiple Input parameter");
    }
}
