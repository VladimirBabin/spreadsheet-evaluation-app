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

class EqFormulaApplierTest {

    private final EqFormulaApplier eqFormulaApplier = new EqFormulaApplier();
    private final InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();
    private Sheet<Input> sheet;
    private Input firstCell;
    private Input secondCell;
    private Input thirdCell;
    private Input expected;
    private FormulaInfo formulaInfo;

    @SuppressWarnings("unchecked")
    //It's safe to cast to generified type for Mock object
    @BeforeEach
    void setUp() {
        sheet = Mockito.mock(Sheet.class);
        firstCell = new Input();
        secondCell = new Input();
        thirdCell = new Input();
        expected = new Input();
    }

    @Test
    public void applyWithTwoEqualParameters() {
        firstCell.setValue(new BigDecimal("10.74"));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal("10.74"));
        secondCell.setType(Type.NUMERIC);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=EQ(10.74, 10.74)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = eqFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithTwoNonEqualParameters() {
        firstCell.setValue(new BigDecimal("10.74"));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal("10.75"));
        secondCell.setType(Type.NUMERIC);

        expected.setValue(false);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=EQ(10.74, 10.75)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = eqFormulaApplier.apply(formulaInfo, sheet);
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

        formulaInfo = new FormulaInfo("=EQ(5, 4, 3)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = eqFormulaApplier.apply(formulaInfo, sheet);

        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: There has to be two parameters for EQ formula", result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);
        secondCell.setValue(new BigDecimal(22));
        secondCell.setType(Type.NUMERIC);
        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=EQ(String, 22)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = eqFormulaApplier.apply(formulaInfo, sheet);

        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}