package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Report;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportWriterTest {
    private final InputChecker inputChecker = new InputChecker();
    private final ReportWriter reportWriter = new ReportWriter(inputChecker);
    private Sheet<Input> sheet;

    @BeforeEach
    public void setUp() {
        sheet = new Sheet<>();
    }

    @Test
    public void testReportOnSheetWithEmptyArrayDataForEmptyCorrectResult() {
        sheet.setId("sheet-0");
        sheet.setData(null);

        Report res = reportWriter.check(sheet);
        assertTrue(res.getOk());
    }

    @Test
    public void testReportOnSheetWithEmptyArrayDataForNotEmptyCorrectResult() {
        sheet.setId("sheet-1");
        sheet.setData(null);

        Report res = reportWriter.check(sheet);
        assertFalse(res.getOk());
        assertEquals("invalid result: invalid sheet "
                        + sheet.getId()
                        + ", should be not null",
                res.getError());
    }

    @Test
    public void testReportOnValidSheet() {
        Input i1 = new Input(2);
        Input i2 = new Input(4);
        Input i3 = new Input(8);
        Input i4 = new Input(16);
        i1.setType(Type.NUMERIC);
        i2.setType(Type.NUMERIC);
        i3.setType(Type.NUMERIC);
        i4.setType(Type.NUMERIC);

        List<Input> innerList = new ArrayList<>();
        innerList.add(i1);
        innerList.add(i2);
        innerList.add(i3);
        innerList.add(i4);

        List<List<Input>> outerList = new ArrayList<>();
        outerList.add(innerList);
        sheet.setId("sheet-1");
        sheet.setData(outerList);

        Report res = reportWriter.check(sheet);
        assertTrue(res.getOk());
    }

    @Test
    public void testReportOnSheetWithTypeError() {
        Input i1 = new Input(2);
        Input i2 = new Input(4);
        Input i3 = new Input(8);
        Input i4 = new Input(16);
        i1.setType(Type.NUMERIC);
        i2.setType(Type.NUMERIC);
        i3.setType(Type.NUMERIC);
        i4.setType(Type.BOOLEAN);

        List<Input> innerList = new ArrayList<>();
        innerList.add(i1);
        innerList.add(i2);
        innerList.add(i3);
        innerList.add(i4);

        List<List<Input>> outerList = new ArrayList<>();
        outerList.add(innerList);
        sheet.setId("sheet-1");
        sheet.setData(outerList);

        Report res = reportWriter.check(sheet);
        assertFalse(res.getOk());
        assertEquals("invalid result: invalid type in cell "
                        + "D1"
                        + ", should be " + Type.NUMERIC,
                res.getError());
    }

    @Test
    public void testReportOnSheetWithValueError() {
        Input i1 = new Input(2);
        Input i2 = new Input(4);
        Input i3 = new Input(8);
        Input i4 = new Input(15);
        i1.setType(Type.NUMERIC);
        i2.setType(Type.NUMERIC);
        i3.setType(Type.NUMERIC);
        i4.setType(Type.NUMERIC);

        List<Input> innerList = new ArrayList<>();
        innerList.add(i1);
        innerList.add(i2);
        innerList.add(i3);
        innerList.add(i4);

        List<List<Input>> outerList = new ArrayList<>();
        outerList.add(innerList);
        sheet.setId("sheet-1");
        sheet.setData(outerList);

        Report res = reportWriter.check(sheet);
        assertFalse(res.getOk());
        assertEquals("invalid result: invalid value in cell "
                        + "D1"
                        + ", value " + 15 + " is incorrect",
                res.getError());
    }
}