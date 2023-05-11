package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The FormulaComputer class computes the formula with the help of abstract FormulaApplies class, which delegates the
 * computation to the right FormulaApplier judging by the name of the formula. It then resolves parameters if
 * formula has them and applies the formula. FormulaComputer checks and recursively computes the formula for parameters
 * and formula results if the result of first computation has another formula inside.
 */
@Service
public class FormulaComputer {
    @Autowired
    private Map<String, FormulaApplier> map;
    private final InputTypeDeterminer inputTypeDeterminer;

    private final Set<String> notationsSet = new LinkedHashSet<>();
    private Logger logger = LoggerFactory.getLogger(FormulaComputer.class);

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
            if (notationsSet.contains(formulaInfo.getFormulaContents())) {
                Input errorCell = new Input("#ERROR: Circular reference");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
            notationsSet.add(formulaInfo.getFormulaContents());
        }
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
        notationsSet.clear();
        return resultOfFormulaComputation;
    }

    private Input resolveParameter(Object rawParameter, Sheet<Input> sheet) {
        Input resolvedParameter = new Input(rawParameter);
        resolvedParameter = inputTypeDeterminer.determineType(resolvedParameter);
        Type resolvedParameterType = resolvedParameter.getType();
        if (resolvedParameterType.equals(Type.FORMULA) || resolvedParameterType.equals(Type.NOTATION)) {
            if (notationsSet.contains(rawParameter.toString())) {
                Input errorCell = new Input("#ERROR: Circular reference");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
            resolvedParameter = computeFormula(resolvedParameter, sheet);
            notationsSet.add(rawParameter.toString());
        }
        return resolvedParameter;
    }
}
