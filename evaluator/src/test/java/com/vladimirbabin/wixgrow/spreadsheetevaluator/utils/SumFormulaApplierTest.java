package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SumFormulaApplierTest {
    private final SumFormulaApplier sumFormulaApplier = new SumFormulaApplier();
    private final InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();

    private Sheet sheet;
    private Input firstCell;
    private Input secondCell;
    private Input thirdCell;
    private Input expected;
    private FormulaInfo formulaInfo;

    @BeforeEach
    void setUp() {
        sheet = Mockito.mock(Sheet.class);
        firstCell = new Input();
        secondCell = new Input();
        thirdCell = new Input();
        expected = new Input();
    }

    @Test
    void applyWithTwoParameters() {
        firstCell.setValue(22);
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(212212);
        secondCell.setType(Type.NUMERIC);

        expected.setValue(new BigDecimal(212234));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=SUM(22, 212212)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = sumFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParameters() {
        firstCell.setValue(221212);
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(22);
        secondCell.setType(Type.NUMERIC);
        thirdCell.setValue(212);
        thirdCell.setType(Type.NUMERIC);

        expected.setValue(new BigDecimal(221446));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        formulaInfo = new FormulaInfo("=SUM(221212, 22, 212)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = sumFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);
        secondCell.setValue(212212);
        secondCell.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell);
        formulaInfo = new FormulaInfo("=SUM(String, 212212)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = sumFormulaApplier.apply(formulaInfo, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }

    @Test
    public void applyForInputWithCircularReference() {
        firstCell.setValue(1);
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue("=C1");
        secondCell.setType(Type.NOTATION);
        thirdCell.setValue("=SUM(A1, B1)");
        thirdCell.setType(Type.FORMULA);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        formulaInfo = new FormulaInfo("=SUM(1, =C1, =SUM(A1, B1))");
        formulaInfo.setResolvedParameters(parameters);

        Input result = sumFormulaApplier.apply(formulaInfo, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}