package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;

import java.util.List;

public interface FormulaApplier {
    default Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        Input errorCell = new Input("#ERROR: Multiple parameters can't be applied for " + this.getClass().getName());
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
    default Input apply(Input singleParameter, Sheet<Input> sheet) {
        Input errorCell = new Input("#ERROR: Single Input parameter can't be applied for " + this.getClass().getName());
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
    default Input apply(String formulaContents, Sheet<Input> sheet) {
        Input errorCell = new Input("#ERROR: Single String parameter can't be applied for " + this.getClass().getName());
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
}
