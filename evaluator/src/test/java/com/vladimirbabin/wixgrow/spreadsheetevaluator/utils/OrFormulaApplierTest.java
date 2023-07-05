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

class OrFormulaApplierTest {

    private final OrFormulaApplier orFormulaApplier = new OrFormulaApplier();
    private final InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();

    private Sheet<Input> sheet;
    private Input firstCell;
    private Input secondCell;
    private Input thirdCell;
    private Input expected;
    private FormulaInfo formulaInfo;

    @BeforeEach
    @SuppressWarnings("unchecked")
    //It's safe to cast to generified type for Mock object
    void setUp() {
        sheet = Mockito.mock(Sheet.class);
        firstCell = new Input();
        secondCell = new Input();
        thirdCell = new Input();
        expected = new Input();
    }

    @Test
    void applyWithTwoParametersWhenOneIsTrue() {
        firstCell.setValue(true);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(false);
        secondCell.setType(Type.BOOLEAN);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=OR(true, false)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = orFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParametersWhereOneIsTrue() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(false);
        secondCell.setType(Type.BOOLEAN);
        thirdCell.setValue(true);
        thirdCell.setType(Type.BOOLEAN);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        formulaInfo = new FormulaInfo("=OR(false, false, true)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = orFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParametersWhereNoneIsTrue() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(false);
        secondCell.setType(Type.BOOLEAN);
        thirdCell.setValue(false);
        thirdCell.setType(Type.BOOLEAN);

        expected.setValue(false);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        formulaInfo = new FormulaInfo("=OR(false, false, false)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = orFormulaApplier.apply(formulaInfo, sheet);
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

        List<Input> parameters = List.of(firstCell, secondCell);
        formulaInfo = new FormulaInfo("=OR(false, 212212)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = orFormulaApplier.apply(formulaInfo, sheet);
        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}