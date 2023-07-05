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

public class MultiplyFormulaApplierTest {

    private final MultiplyFormulaApplier multiplyFormulaApplier = new MultiplyFormulaApplier();
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
    public void applyWithTwoParameters() {
        firstCell.setValue(22);
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(22);
        secondCell.setType(Type.NUMERIC);

        expected.setValue(new BigDecimal(22*22));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=MULTIPLY(22, 22)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = multiplyFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParameters() {
        firstCell.setValue(22);
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(22);
        secondCell.setType(Type.NUMERIC);
        thirdCell.setValue(22);
        thirdCell.setType(Type.NUMERIC);

        Input expected = new Input(new BigDecimal(22*22*22));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        formulaInfo = new FormulaInfo("=MULTIPLY(22, 22, 22)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = multiplyFormulaApplier.apply(formulaInfo, sheet);
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

        formulaInfo = new FormulaInfo("=MULTIPLY(String, 212212)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = multiplyFormulaApplier.apply(formulaInfo, sheet);
        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}