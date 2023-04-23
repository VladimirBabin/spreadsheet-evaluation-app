package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GtFormulaApplierTest {
    @Autowired
    private GtFormulaApplier gtFormulaApplier;

    @Autowired
    private InputTypeDeterminer inputTypeDeterminer;

    private Sheet sheet;
    private Input firstCell;
    private Input secondCell;
    private Input thirdCell;
    private Input expected;

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

        Input result = gtFormulaApplier.apply(parameters, sheet);
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

        Input result = gtFormulaApplier.apply(parameters, sheet);
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

        Input result = gtFormulaApplier.apply(parameters, sheet);

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

        Input result = gtFormulaApplier.apply(parameters, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}