package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("MULTIPLY")
public class MultiplyFormulaApplier implements FormulaApplier {

    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        List<Input> resolvedParameters = formulaInfo.getResolvedParameters();
        if (!validateParametersTypes(resolvedParameters, Type.NUMERIC)) {
            return errorCell("Invalid parameter type");
        }

        BigDecimal resultOfMultiplication = resolvedParameters
                .stream()
                .map(i -> i.getValue().toString())
                .map(BigDecimal::new)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);

        Input cellResult = new Input(resultOfMultiplication);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }
}
