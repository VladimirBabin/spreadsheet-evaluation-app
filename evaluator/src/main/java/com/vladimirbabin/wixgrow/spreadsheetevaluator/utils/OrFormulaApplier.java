package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("OR")
public class OrFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        List<Input> resolvedParameters = formulaInfo.getResolvedParameters();
        if (!validateParametersTypes(resolvedParameters, Type.BOOLEAN)) {
            return errorCell("Invalid parameter type");
        }
        Optional<Boolean> optional = resolvedParameters.stream()
                .map(i -> i.getValue().toString())
                .map(Boolean::parseBoolean)
                .reduce(Boolean::logicalOr);

        Boolean resultOfAndFormula;
        if (optional.isPresent()) {
            resultOfAndFormula = optional.get();
        } else {
            return errorCell("The OR formula result can't be null");
        }
        Input cellResult = new Input(resultOfAndFormula);
        return cellResult;
    }
}
