package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("AND")
public class AndFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        if (!validateParametersTypes(formulaInfo.getResolvedParameters(), Type.BOOLEAN)) {
            return errorCell("Invalid parameter type");
        }

        Optional<Boolean> optional = formulaInfo.getResolvedParameters().stream()
                .map(i -> i.getValue().toString())
                .map(Boolean::parseBoolean)
                .reduce(Boolean::logicalAnd);

        Boolean resultOfAndFormula;

        if (optional.isPresent()) {
            resultOfAndFormula = optional.get();
        } else {
            return errorCell("The AND formula result can't be null");
        }

        Input cellResult = new Input(resultOfAndFormula);
        cellResult.setType(Type.BOOLEAN);
        return cellResult;
    }
}
