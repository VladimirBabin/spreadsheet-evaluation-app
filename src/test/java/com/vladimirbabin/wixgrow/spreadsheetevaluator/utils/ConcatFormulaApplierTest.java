//package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;
//
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class ConcatFormulaApplierTest {
//    @Autowired
//    ConcatFormulaApplier concatFormulaApplier;
//
//    @Autowired
//    InputTypeDeterminer inputTypeDeterminer;
//
//    private Sheet sheet;
//    private Input firstCell;
//    private Input secondCell;
//    private Input thirdCell;
//    private Input expected;
//
//    @BeforeEach
//    void setUp() {
//        sheet = Mockito.mock(Sheet.class);
//        firstCell = new Input();
//        secondCell = new Input();
//        thirdCell = new Input();
//        expected = new Input();
//    }
//
//    @Test
//    void applyWithTwoStringParameters() {
//        firstCell.setValue("Hello ");
//        firstCell.setType(Type.STRING);
//        secondCell.setValue("- Hi");
//        secondCell.setType(Type.STRING);
//
//        expected.setValue("Hello - Hi");
//        expected.setType(Type.STRING);
//
//        List<Input> parameters = List.of(firstCell, secondCell);
//
//        Input result = concatFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    void applyWithThreeStringParametersWithPunctuationMarks() {
//        firstCell.setValue("Hello");
//        firstCell.setType(Type.STRING);
//        secondCell.setValue(", ");
//        secondCell.setType(Type.STRING);
//        thirdCell.setValue("World!");
//        thirdCell.setType(Type.STRING);
//
//        expected.setValue("Hello, World!");
//        expected.setType(Type.STRING);
//
//        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
//
//        Input result = concatFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    public void applyWithStringAndNumericAndBooleanParameters() {
//        firstCell.setValue("All ");
//        firstCell.setType(Type.STRING);
//        secondCell.setValue(22);
//        secondCell.setType(Type.NUMERIC);
//        thirdCell.setValue(false);
//        thirdCell.setType(Type.BOOLEAN);
//
//        expected.setValue("All 22false");
//        expected.setType(Type.STRING);
//
//        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
//
//        Input result = concatFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    public void applyWithInvalidParameter() {
//        firstCell.setValue("A1");
//        firstCell.setType(Type.NOTATION);
//        secondCell.setValue(212212);
//        secondCell.setType(Type.NUMERIC);
//
//        List<Input> parameters = List.of(firstCell, secondCell);
//        Input result = concatFormulaApplier.apply(parameters, sheet);
//
//        assertTrue(result.getType().equals(Type.ERROR));
//        assertEquals("#ERROR: Invalid parameter type", result.getValue());
//    }
//}