package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.utils.FormulaComputer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class FormulaComputerTest {

    @Autowired
    private FormulaComputer formulaComputer;

    @Value("${spreadsheet.json.sheet-13.not-with-notation}")
    private String sheetWithNotFormulaWithNotationInside;

    @Value("${spreadsheet.json.notations-referencing}")
    private String sheetWithNotationsReferencingNotations;

    @Value("${spreadsheet.json.notations-with-circular-reference}")
    private String sheetWithCircularReferencedNotations;

    private Input cell;
    private Input cell2;
    private Sheet sheet;



    @Test
    public void applyForNotFormulaWithNotationInside() {
        cell = new Input("=NOT(D1)");
        cell2 = new Input("=NOT(E2)");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotFormulaWithNotationInside, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        Input result = formulaComputer.computeFormula(cell, sheet);
        Input result2 = formulaComputer.computeFormula(cell2, sheet);

        assertEquals(Type.BOOLEAN, result.getType());
        assertEquals(true, result.getValue());

        assertEquals(Type.BOOLEAN, result2.getType());
        assertEquals(false, result2.getValue());
    }

    @Test
    public void applyForNotationsChainFormula() {
        cell = new Input("=A6");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotationsReferencingNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        Input result = formulaComputer.computeFormula(cell, sheet);
        assertEquals(Type.STRING, result.getType());
        assertEquals("First", result.getValue());
    }

    @Test
    public void applyForSheetWithCircularReference() {
        cell = new Input("=B1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithCircularReferencedNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        Input result = formulaComputer.computeFormula(cell, sheet);
        assertEquals(Type.ERROR, result.getType());
        assertEquals("#ERROR: Circular reference", result.getValue());
    }

}