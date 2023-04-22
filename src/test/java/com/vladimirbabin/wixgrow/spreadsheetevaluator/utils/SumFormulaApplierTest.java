package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SumFormulaApplierTest {
    private final InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();
    private final SumFormulaApplier sumFormulaApplier = new SumFormulaApplier();
    private FormulaComputer formulaComputer = new FormulaComputer(inputTypeDeterminer);

    @Test
    void applyForSheetWithTwoParameters() {

        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input(22);
        firstCell.setType(Type.NUMERIC);
        Input secondCell = new Input(212212);
        firstCell.setType(Type.NUMERIC);
        Input thirdCell = new Input("=SUM(A1, B1)");
        firstCell.setType(Type.FORMULA);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithTwoParametersFormula.setData(data);

        Input expected = new Input(new BigDecimal(212234));
        expected.setType(Type.NUMERIC);

        Input result = formulaComputer.computeFormula(thirdCell, sheetWithTwoParametersFormula);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyForSheetWithThreeParametersFormula() {
        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input(221212);
        firstCell.setType(Type.NUMERIC);
        Input secondCell = new Input(22);
        firstCell.setType(Type.NUMERIC);
        Input thirdCell = new Input("=SUM(A1, B1, D1)");
        firstCell.setType(Type.FORMULA);
        Input forthCell = new Input(212);
        forthCell.setType(Type.NUMERIC);
        data.add(List.of(firstCell, secondCell, thirdCell, forthCell));
        sheetWithTwoParametersFormula.setData(data);

        List<Input> parametersList = new ArrayList<>();
        Input firstParameter = new Input("A1");
        firstParameter.setType(Type.NOTATION);
        Input secondParameter = new Input("B1");
        secondParameter.setType(Type.NOTATION);
        Input thirdParameter = new Input("D1");
        thirdParameter.setType(Type.NOTATION);
        parametersList.add(firstParameter);
        parametersList.add(secondParameter);
        parametersList.add(thirdParameter);

        Input expected = new Input(new BigDecimal(221446));
        expected.setType(Type.NUMERIC);

        assertEquals(expected.getType(), sumFormulaApplier.apply(parametersList, sheetWithTwoParametersFormula).getType());
        assertEquals(expected.getValue(), sumFormulaApplier.apply(parametersList, sheetWithTwoParametersFormula).getValue());
    }

    @Test
    public void applyForSheetWithNumberAndNotationParametersFormula() {
        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input(22);
        firstCell.setType(Type.NUMERIC);
        Input secondCell = new Input(212212);
        firstCell.setType(Type.NUMERIC);
        Input thirdCell = new Input("=SUM(A1, 6, B1)");
        firstCell.setType(Type.FORMULA);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithTwoParametersFormula.setData(data);

        List<Input> parametersList = new ArrayList<>();
        Input firstParameter = new Input("A1");
        firstParameter.setType(Type.NOTATION);
        Input secondParameter = new Input(6);
        secondParameter.setType(Type.NUMERIC);
        Input thirdParameter = new Input("B1");
        thirdParameter.setType(Type.NOTATION);
        parametersList.add(firstParameter);
        parametersList.add(secondParameter);
        parametersList.add(thirdParameter);
        Input expected = new Input(new BigDecimal(212240));
        expected.setType(Type.NUMERIC);

        assertEquals(expected.getType(), sumFormulaApplier.apply(parametersList, sheetWithTwoParametersFormula).getType());
        assertEquals(expected.getValue(), sumFormulaApplier.apply(parametersList, sheetWithTwoParametersFormula).getValue());
    }
}