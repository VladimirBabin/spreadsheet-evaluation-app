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
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        //todo: duplicate with OrFormula, u can think about abstract class for them
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
        //todo end of duplicate

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
