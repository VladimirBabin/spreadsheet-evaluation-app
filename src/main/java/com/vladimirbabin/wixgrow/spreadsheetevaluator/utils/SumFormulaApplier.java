package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component("SUM")
public class SumFormulaApplier implements FormulaApplier {
    Logger logger = LoggerFactory.getLogger(SumFormulaApplier.class);

    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        for (Input parameter : resolvedParameters) {
            logger.info(parameter.getType().toString() + " " + parameter.getValue());
            if (!parameter.getType().equals(Type.NUMERIC)) {
                logger.error("Invalid parameter type");
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }
        BigDecimal resultOfSum = resolvedParameters.stream()
                .map(i -> i.getValue().toString())
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Input cellResult = new Input(resultOfSum);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }

//    private BigDecimal checkIfNotationAndCompute(String parameter, Sheet<Cell> sheet) {
//        Cell tempCell = null;
//        FormulaInfo formulaInfo = new FormulaInfo(parameter);
//        if (NumberUtils.isParsable(parameter)) {
//            return new BigDecimal(parameter);
//        } else {
//            try {
//                tempCell = sheet.getElementByNotation(parameter);
//                tempCell.setType(Type.NUMERIC);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException("Sheet doesn't contain such notation");
//            }
//        }
//        if (tempCell == null || !tempCell.getType().equals(Type.NUMERIC)) {
//            throw new RuntimeException("Null or not compatible cell type");
//        }
//        return new BigDecimal(tempCell.getValue().toString());
//    }
}
