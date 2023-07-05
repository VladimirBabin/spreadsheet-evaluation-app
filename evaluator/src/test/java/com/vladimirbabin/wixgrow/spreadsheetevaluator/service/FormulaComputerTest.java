package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.utils.FormulaComputer;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// The casts when deserializing json values are correct because the Sheet can always be generalized with Object type
@SuppressWarnings("unchecked")
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class FormulaComputerTest {

    @Autowired
    private FormulaComputer formulaComputer;

    @Autowired
    private SheetCellsDeterminer sheetCellsDeterminer;

    @Value("${spreadsheet.json.sheet-13.not-with-notation}")
    private String sheetWithNotFormulaWithNotationInside;

    @Value("${spreadsheet.json.sheet-18.formula-inside-formula}")
    private String sheetWithFormulaWithAnotherFormulaInside;

    @Value("${spreadsheet.json.sheet-20.concat-with-notations-inside}")
    private String sheetWithConcatFormulaWithNotationsInside;

    @Value("${spreadsheet.json.sheet-21.notations-referencing-in-single-array}")
    private String sheetWithNotationReferencingInSingleArray;

    @Value("${spreadsheet.json.sheet-22.notations-referencing-in-separate-arrays}")
    private String sheetWithNotationsReferencingNotationsInSeparateArrays;

    @Value("${spreadsheet.json.sheet-23.notations-referencing-in-single-array-backwards}")
    private String sheetWithConcatFormulaWithNotationsBackwards;

    @Value("${spreadsheet.json.sheet-31.notations-with-single-circular-reference}")
    private String getSheetWithCircularReferencedSingleNotation;

    @Value("${spreadsheet.json.sheet-32.notations-with-circular-reference}")
    private String sheetWithCircularReferencedNotations;

    @Value("${spreadsheet.json.sheet-33.circular-reference-between-notation-and-formula}")
    private String sheetWithCircularReferenceBetweenNotationAndFormula;

    private Input cell;
    private Sheet<Object> sheet;

    private Sheet<Input> inputSheet;



    @Test
    public void applyForNotFormulaWithNotationInside() {
        cell = new Input("=NOT(D1)");
        Input cell2 = new Input("=NOT(E2)");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotFormulaWithNotationInside, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        Input result2 = formulaComputer.computeFormula(cell2, inputSheet);

        assertEquals(Type.BOOLEAN, result.getType());
        assertEquals(true, result.getValue());

        assertEquals(Type.BOOLEAN, result2.getType());
        assertEquals(false, result2.getValue());
    }

    @Test
    public void applyForFormulaInsideAnotherFormula() {
        cell = new Input("=IF(GT(A1, B1), A1, B1)");

        try {
            sheet = new ObjectMapper().readValue(sheetWithFormulaWithAnotherFormulaInside, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.NUMERIC, result.getType());
        assertEquals(21221, result.getValue());
    }

    @Test
    public void applyForConcatFormulaWithNotationsInside() {
        cell = new Input("=CONCAT(I1, \" is \", I2)");

        try {
            sheet = new ObjectMapper().readValue(sheetWithConcatFormulaWithNotationsInside, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.STRING, result.getType());
        assertEquals("AN is Netherlands Antilles", result.getValue());
    }

    @Test
    public void applyForNotationsChainInSingleArray() {
        cell = new Input("=G1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotationReferencingInSingleArray, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.STRING, result.getType());
        assertEquals("First", result.getValue());
    }

    @Test
    public void applyForNotationsChainInSeparateArrays() {
        cell = new Input("=A6");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotationsReferencingNotationsInSeparateArrays, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.STRING, result.getType());
        assertEquals("First", result.getValue());
    }

    @Test
    public void applyForNotationsChainBackwards() {
        cell = new Input("=B1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithConcatFormulaWithNotationsBackwards, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.STRING, result.getType());
        assertEquals("Last", result.getValue());
    }

    @Test
    public void applyForSheetWithSingleCircularReference() {
        cell = new Input("=A1");

        try {
            sheet = new ObjectMapper().readValue(getSheetWithCircularReferencedSingleNotation, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.ERROR, result.getType());
        assertEquals("#ERROR: Circular reference", result.getValue());
    }

    @Test
    public void applyForSheetWithCircularReference() {
        cell = new Input("=B1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithCircularReferencedNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.ERROR, result.getType());
        assertEquals("#ERROR: Circular reference", result.getValue());
    }

    @Test
    public void applyForSheetWithCircularReferenceBetweenNotationAndFormula() {
        cell = new Input("=C1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithCircularReferenceBetweenNotationAndFormula, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        inputSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        Input result = formulaComputer.computeFormula(cell, inputSheet);
        assertEquals(Type.ERROR, result.getType());
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}