package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class MultiplyFormulaApplierTest {

    @Test
    void applyForSheetWithTwoParameters() {
        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input("=MULTIPLY(B1, C1)");
        firstCell.setType(Type.FORMULA);
        Input secondCell = new Input(22);
        firstCell.setType(Type.NUMERIC);
        Input thirdCell = new Input(212);
        firstCell.setType(Type.NUMERIC);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithTwoParametersFormula.setData(data);

        List<Input> parametersList = new ArrayList<>();
        Input firstParameter = new Input("B1");
        firstParameter.setType(Type.NOTATION);
        Input secondParameter = new Input("C1");
        secondParameter.setType(Type.NOTATION);
        parametersList.add(firstParameter);
        parametersList.add(secondParameter);
        Input expected = new Input(new BigDecimal(4664));
        expected.setType(Type.NUMERIC);

        MultiplyFormulaApplier multiplyFormulaApplier = new MultiplyFormulaApplier();
        Assertions.assertEquals(expected.getType(), multiplyFormulaApplier.apply(parametersList, sheetWithTwoParametersFormula).getType());
        Assertions.assertEquals(expected.getValue(), multiplyFormulaApplier.apply(parametersList, sheetWithTwoParametersFormula).getValue());
    }
}