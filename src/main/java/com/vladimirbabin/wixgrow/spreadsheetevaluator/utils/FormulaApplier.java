package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;

import java.util.List;

public interface FormulaApplier {
    Input apply(List<Input> resolvedParameters, Sheet<Input> sheet);
    Input apply(Input singleParameter, Sheet<Input> sheet);
    Input apply(String formulaContents, Sheet<Input> sheet);
}
