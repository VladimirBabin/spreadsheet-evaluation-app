package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FormulaComputer {
    @Autowired
    private Map<String, FormulaApplier> map;
    private final InputTypeDeterminer inputTypeDeterminer;

    public FormulaComputer(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Input computeFormula(Input input, Sheet<Input> sheet) {
        FormulaInfo formulaInfo = new FormulaInfo(input.getValue().toString());
        FormulaApplier formulaApplier = map.get(formulaInfo.getFormulaName());
        if (formulaApplier == null) {
            throw new UnsupportedOperationException(formulaInfo.getFormulaName() + " not supported");
        }
        Input resultOfFormulaComputation;
        if (!formulaInfo.hasParameters()) {
            resultOfFormulaComputation = formulaApplier.apply(formulaInfo.getFormulaContents(), sheet);
        } else if (formulaInfo.hasSingleParameter()) {
            Input resolvedParameter = resolveParameter(formulaInfo.getFormulaContents(), sheet);
            resultOfFormulaComputation = formulaApplier.apply(resolvedParameter, sheet);
        } else {
            List<Input> resolvedParameters = new ArrayList<>();
            List<String> rawParameters = formulaInfo.getArrayOfParameters();
            for (Object rawParameter : rawParameters) {
                Input resolvedParameter = resolveParameter(rawParameter, sheet);
                resolvedParameters.add(resolvedParameter);
            }
            resultOfFormulaComputation = formulaApplier.apply(resolvedParameters, sheet);
        }
        resultOfFormulaComputation = inputTypeDeterminer.determineType(resultOfFormulaComputation);
        if (resultOfFormulaComputation.getType().equals(Type.FORMULA)) {
            resultOfFormulaComputation = computeFormula(resultOfFormulaComputation, sheet);
        }
        return resultOfFormulaComputation;
    }

    private Input resolveParameter(Object rawParameter, Sheet<Input> sheet) {
        Input resolvedParameter = new Input(rawParameter);
        resolvedParameter = inputTypeDeterminer.determineType(resolvedParameter);
        Type resolvedParameterType = resolvedParameter.getType();
        if (resolvedParameterType.equals(Type.FORMULA) || resolvedParameterType.equals(Type.NOTATION)) {
            resolvedParameter = computeFormula(resolvedParameter, sheet);
        }
        return resolvedParameter;
    }
}
