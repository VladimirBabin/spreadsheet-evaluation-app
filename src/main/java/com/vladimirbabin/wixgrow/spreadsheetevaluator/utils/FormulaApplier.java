package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;

public interface FormulaApplier {
    Cell apply(FormulaInfo formulaInfo, Sheet<Cell> sheet);
}
