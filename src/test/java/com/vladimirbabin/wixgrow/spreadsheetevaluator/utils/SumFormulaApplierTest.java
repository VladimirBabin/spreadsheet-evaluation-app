package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SumFormulaApplierTest {
    @Autowired
    SumFormulaApplier sumFormulaApplier;

    @Autowired
    InputTypeDeterminer inputTypeDeterminer;

    @Test
    void applyWithTwoParameters() {
        Sheet<Input> sheetWithTwoParametersFormula = new Sheet<>();
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input(22);
        firstCell.setType(Type.NUMERIC);
        Input secondCell = new Input(212212);
        secondCell.setType(Type.NUMERIC);
        Input thirdCell = new Input("=SUM(A1, B1)");
        thirdCell.setType(Type.FORMULA);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithTwoParametersFormula.setData(data);

        Input expected = new Input(new BigDecimal(212234));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = sumFormulaApplier.apply(parameters, sheetWithTwoParametersFormula);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParameters() {
        Sheet<Input> sheetWithThreeParametersFormula = new Sheet<>();
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input(221212);
        firstCell.setType(Type.NUMERIC);
        Input secondCell = new Input(22);
        secondCell.setType(Type.NUMERIC);
        Input thirdCell = new Input("=SUM(A1, B1, D1)");
        thirdCell.setType(Type.FORMULA);
        Input forthCell = new Input(212);
        forthCell.setType(Type.NUMERIC);
        data.add(List.of(firstCell, secondCell, thirdCell, forthCell));
        sheetWithThreeParametersFormula.setData(data);

        Input expected = new Input(new BigDecimal(221446));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell, forthCell);

        Input result = sumFormulaApplier.apply(parameters, sheetWithThreeParametersFormula);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        Sheet<Input> sheetWithInvalidInputFormula = new Sheet<>();
        List<List<Input>> data = new ArrayList<>();
        Input firstCell = new Input("String");
        firstCell.setType(Type.STRING);
        Input secondCell = new Input(212212);
        secondCell.setType(Type.NUMERIC);
        Input thirdCell = new Input("=SUM(A1, 6, B1)");
        thirdCell.setType(Type.FORMULA);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithInvalidInputFormula.setData(data);

        List<Input> parameters = List.of(firstCell, secondCell);
        Input result = sumFormulaApplier.apply(parameters, sheetWithInvalidInputFormula);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}