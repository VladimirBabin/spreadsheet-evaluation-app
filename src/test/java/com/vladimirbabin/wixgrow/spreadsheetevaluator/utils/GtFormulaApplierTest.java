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

class GtFormulaApplierTest {

    private final GtFormulaApplier gtFormulaApplier = new GtFormulaApplier();
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
    public void applyWithTwoParametersWhereFirstIsGreater() {
        firstCell.setValue(new BigDecimal(3));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal(1));
        secondCell.setType(Type.NUMERIC);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=GT(3, 1)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = gtFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithTwoParametersWhereFirstIsNotGreater() {
        firstCell.setValue(new BigDecimal(1));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal(3));
        secondCell.setType(Type.NUMERIC);

        expected.setValue(false);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=GT(1, 3)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = gtFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParameters() {
        firstCell.setValue(new BigDecimal(5));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal(4));
        secondCell.setType(Type.NUMERIC);
        thirdCell.setValue(new BigDecimal(3));
        thirdCell.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        formulaInfo = new FormulaInfo("=GT(5, 4, 3)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = gtFormulaApplier.apply(formulaInfo, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: There has to be two parameters for GT formula", result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);
        secondCell.setValue(new BigDecimal(22));
        secondCell.setType(Type.NUMERIC);
        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=GT(String, 22)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = gtFormulaApplier.apply(formulaInfo, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}