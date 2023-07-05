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

class NotFormulaApplierTest {

    private final NotFormulaApplier notFormulaApplier = new NotFormulaApplier();
    private final InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();

    private Sheet<Input> sheet;
    private Input firstCell;
    private Input secondCell;
    private Input expected;
    private FormulaInfo formulaInfo;

    @BeforeEach
    @SuppressWarnings("unchecked")
    //It's safe to cast to generified type for Mock object
    void setUp() {
        sheet = Mockito.mock(Sheet.class);
        firstCell = new Input();
        secondCell = new Input();
        expected = new Input();
    }

    @Test
    public void applyWithOneParameter() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell);

        formulaInfo = new FormulaInfo("=NOT(false)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = notFormulaApplier.apply(formulaInfo, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithTwoParameters() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(true);
        secondCell.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        formulaInfo = new FormulaInfo("=NOT(false, true)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = notFormulaApplier.apply(formulaInfo, sheet);
        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: There has to be only one parameter for NOT formula"
                , result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);

        List<Input> parameters = List.of(firstCell);

        formulaInfo = new FormulaInfo("=NOT(String)");
        formulaInfo.setResolvedParameters(parameters);

        Input result = notFormulaApplier.apply(formulaInfo, sheet);
        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}