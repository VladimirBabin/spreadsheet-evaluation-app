package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AndFormulaApplierTest {

    private final AndFormulaApplier andFormulaApplier = new AndFormulaApplier();
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
    void applyWithTwoParametersWhenBothAreTrue() {
        firstCell.setValue(true);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(true);
        secondCell.setType(Type.BOOLEAN);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        formulaInfo = new FormulaInfo("=AND(true, true)");
        List<Input> parameters = List.of(firstCell, secondCell);
        formulaInfo.setResolvedParameters(parameters);

        Input result = andFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParametersWhereOnlyOneIsTrue() {
        firstCell.setValue(true);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(false);
        secondCell.setType(Type.BOOLEAN);
        thirdCell.setValue(true);
        thirdCell.setType(Type.BOOLEAN);

        expected.setValue(false);
        expected.setType(Type.BOOLEAN);

        formulaInfo = new FormulaInfo("=AND(true, false, true)");
        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
        formulaInfo.setResolvedParameters(parameters);


        Input result = andFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParametersWhereAllAreTrue() {
        firstCell.setValue(true);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(true);
        secondCell.setType(Type.BOOLEAN);
        thirdCell.setValue(true);
        thirdCell.setType(Type.BOOLEAN);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        formulaInfo = new FormulaInfo("=AND(true, true, true)");
        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
        formulaInfo.setResolvedParameters(parameters);


        Input result = andFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(212212);
        secondCell.setType(Type.NUMERIC);

        formulaInfo = new FormulaInfo("=AND(false, 212212)");
        List<Input> parameters = List.of(firstCell, secondCell);
        formulaInfo.setResolvedParameters(parameters);

        Input result = andFormulaApplier.apply(formulaInfo, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}