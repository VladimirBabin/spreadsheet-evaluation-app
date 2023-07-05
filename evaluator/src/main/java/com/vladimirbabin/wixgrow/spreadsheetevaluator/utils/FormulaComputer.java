package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
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
    private String initialNotation;
    private boolean isFormulaInsideNotation;
    public FormulaComputer(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Input computeFormula(Input input, Sheet<Input> sheet) {
//      Assigning value to formulaInfo
        FormulaInfo formulaInfo = new FormulaInfo(input.getValue().toString());

//      Retrieving the right formulaApplier from the Map
        FormulaApplier formulaApplier = map.get(formulaInfo.getFormulaName());
        if (formulaApplier == null) {
            throw new UnsupportedOperationException(formulaInfo.getFormulaName() + " not supported");
        }

        /* If formula is notation we check for Circular reference and set the notation as the initial one.
           If it's not a notation, we resolve parameters and set them to formulaInfo.
         */
        if (formulaInfo.isNotation()) {
            if (initialNotation != null && initialNotation.equals(formulaInfo.getFormulaContents())) {
                Input errorCell = new Input("#ERROR: Circular reference");
                errorCell.setType(Type.ERROR);
                initialNotation = null;
                isFormulaInsideNotation = false;
                return errorCell;
            }
            if (initialNotation == null) {
                initialNotation = formulaInfo.getFormulaContents();
            }
        } else {
            List<Input> resolvedParameters = resolveParameters(formulaInfo.getArrayOfParameters(), sheet);
            formulaInfo.setResolvedParameters(resolvedParameters);
        }

//      Applying the formula
        Input resultOfFormulaComputation = formulaApplier.apply(formulaInfo, sheet);

        Type foundResultType = resultOfFormulaComputation.getType();
        if (foundResultType.equals(Type.FORMULA) || foundResultType.equals(Type.NOTATION)) {
            if (foundResultType.equals(Type.FORMULA) && initialNotation != null) {
                isFormulaInsideNotation = true;
            }
            resultOfFormulaComputation = computeFormula(resultOfFormulaComputation, sheet);
        }
        if (!isFormulaInsideNotation) {
            initialNotation = null;
        }
        return resultOfFormulaComputation;
    }

    private List<Input> resolveParameters(List<String> rawParameters, Sheet<Input> sheet) {
        List<Input> resolvedParameters = new ArrayList<>();
        for (Object rawParameter : rawParameters) {
            Input resolvedParameter = resolveParameter(rawParameter, sheet);
            resolvedParameters.add(resolvedParameter);
        }
        return resolvedParameters;
    }

    private Input resolveParameter(Object rawParameter, Sheet<Input> sheet) {
        Input resolvedParameter = new Input(rawParameter);
        resolvedParameter = inputTypeDeterminer.determineType(resolvedParameter);
        Type resolvedParameterType = resolvedParameter.getType();
        if (resolvedParameterType.equals(Type.FORMULA) || resolvedParameterType.equals(Type.NOTATION)) {
            if (resolvedParameterType.equals(Type.NOTATION)) {
                if (initialNotation != null && initialNotation.equals(rawParameter.toString())) {
                    Input errorCell = new Input("#ERROR: Circular reference");
                    errorCell.setType(Type.ERROR);
                    initialNotation = null;
                    isFormulaInsideNotation = false;
                    return errorCell;
                }
            }
            resolvedParameter = computeFormula(resolvedParameter, sheet);
        }
        return resolvedParameter;
    }
}
