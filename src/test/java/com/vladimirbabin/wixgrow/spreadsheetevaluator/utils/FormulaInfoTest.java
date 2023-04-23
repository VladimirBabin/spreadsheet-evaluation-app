package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FormulaInfoTest {

    @Test
    void getFormulaName() {
        FormulaInfo sum = new FormulaInfo("=SUM(A1, A2)");
        assertEquals("SUM", sum.getFormulaName());

        FormulaInfo multiply = new FormulaInfo("=MULTIPLY(A1, A2)");
        assertEquals("MULTIPLY", multiply.getFormulaName());

        FormulaInfo divide = new FormulaInfo("=DIVIDE(A1, A2)");
        assertEquals("DIVIDE", divide.getFormulaName());

        FormulaInfo notation = new FormulaInfo("=A1");
        assertEquals("NOTATION", notation.getFormulaName());
    }

    @Test
    void getFormulaContents() {
        FormulaInfo sum = new FormulaInfo("=SUM(A1, A2)");
        assertEquals("A1, A2", sum.getFormulaContents());

        FormulaInfo concat = new FormulaInfo("=CONCAT(\"Hello\", \", \", \"World!\")");
        assertEquals("\"Hello\", \", \", \"World!\"", concat.getFormulaContents());

        FormulaInfo not = new FormulaInfo("=NOT(F1)");
        assertEquals("F1", not.getFormulaContents());

        FormulaInfo notation = new FormulaInfo("=A1");
        assertEquals("A1", notation.getFormulaContents());
    }

    @Test
    void getArrayOfParameters() {
        FormulaInfo sum = new FormulaInfo("=SUM(A1, A2, B4)");
        assertEquals(List.of("A1", "A2", "B4"), sum.getArrayOfParameters());

        FormulaInfo concat = new FormulaInfo("=CONCAT(\"Hello\", \", \", \"World!\")");
        List<Object> expected = List.of("\"Hello\"", "\", \"", "\"World!\"");
        List<Object> resultList = concat.getArrayOfParameters();
        assertEquals(expected, resultList);
    }

    @Test
    void splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes() {
        FormulaInfo formulaInfo = new FormulaInfo("");
        String formulaContents = "\"Hello\", \", \", \"World!\"";
        List<String> expected = List.of("\"Hello\"", "\", \"", "\"World!\"");

        assertEquals(expected, formulaInfo.splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(formulaContents));
    }

    @Test
    void isConcat() {
        FormulaInfo concat = new FormulaInfo("=CONCAT(\"Hello\", \", \", \"World!\"");
        assertTrue(concat.isConcat());

        FormulaInfo notation = new FormulaInfo("=A1");
        assertFalse(notation.isConcat());
    }

    @Test
    void hasSingleParameter() {
        FormulaInfo not = new FormulaInfo("=NOT(F1)");
        assertTrue(not.hasSingleParameter());

        FormulaInfo notation = new FormulaInfo("=A1");
        assertTrue(notation.hasSingleParameter());

        FormulaInfo sum = new FormulaInfo("=SUM(A1, A2, B4)");
        assertFalse(sum.hasSingleParameter());
    }

    @Test
    void isNotation() {
        FormulaInfo notation = new FormulaInfo("=A1");
        assertTrue(notation.isNotation());

        FormulaInfo sum = new FormulaInfo("=SUM(A1, A2, B4)");
        assertFalse(sum.isNotation());
    }

    @Test
    void hasParameters() {
        FormulaInfo notation = new FormulaInfo("=A1");
        assertFalse(notation.hasParameters());

        FormulaInfo sum = new FormulaInfo("=SUM(A1, A2, B4)");
        assertTrue(sum.hasParameters());
    }
}