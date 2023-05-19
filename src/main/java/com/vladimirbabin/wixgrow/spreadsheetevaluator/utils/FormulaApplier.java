package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;

import java.util.List;

public interface FormulaApplier {
    Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet);

    default boolean validateParametersTypes(List<Input> parameters, Type type) {
        for (Input parameter : parameters) {
            if (!parameter.getType().equals(type)) {
                return false;
            }
        }
        return true;
    }

    default Input errorCell(String message) {
        Input errorCell = new Input("#ERROR: " + message);
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
}
