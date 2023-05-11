package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("AND")
public class AndFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        for (Input parameter : formulaInfo.getResolvedParameters()) {
            if (!parameter.getType().equals(Type.BOOLEAN)) {
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }

        Optional<Boolean> optional = formulaInfo.getResolvedParameters().stream()
                .map(i -> i.getValue().toString())
                .map(Boolean::parseBoolean)
                .reduce(Boolean::logicalAnd);

        Boolean resultOfAndFormula;

        if (optional.isPresent()) {
            resultOfAndFormula = optional.get();
        } else {
            Input errorCell = new Input("#ERROR: The AND formula result can't be null");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }

        Input cellResult = new Input(resultOfAndFormula);
        cellResult.setType(Type.BOOLEAN);
        return cellResult;
    }
}
