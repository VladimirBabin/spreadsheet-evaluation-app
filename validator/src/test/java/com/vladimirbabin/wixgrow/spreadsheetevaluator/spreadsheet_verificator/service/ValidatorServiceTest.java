package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.*;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling.NoSubmittedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InputChecker inputChecker = new InputChecker();
    private final ReportWriter reportWriter = new ReportWriter(inputChecker);
    private final ValidatorService service = new ValidatorService(reportWriter);
    private SpreadsheetToVerify spreadsheet;
    private final ClassLoader classLoader = getClass().getClassLoader();
    private final InputStream correctSheets = classLoader.getResourceAsStream("correct-sheets.json");
    private final InputStream incorrectTypeSheets = classLoader.getResourceAsStream("incorrect-type-sheets.json");
    private final InputStream incorrectValueSheets = classLoader.getResourceAsStream("incorrect-value-sheets.json");




    @BeforeEach
    public void setUp() {
        spreadsheet = new SpreadsheetToVerify();
    }

    @Test
    public void testForNullSpreadsheet() {
        Exception exception = assertThrows(NoSubmittedResult.class, () -> service.verifySpreadsheet(null));

        String expectedMessage = "No result submitted, please send the result in json format";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testForEmptySpreadsheet() {
        List<Sheet> objectSheets = new ArrayList<>();
        spreadsheet.setResults(objectSheets);

        ValidatorResponse responseDto = service.verifySpreadsheet(spreadsheet);
        assertNull(responseDto.getMessage());
        assertEquals("invalid  result: " +
                        "the spreadsheet can't be empty"
                , responseDto.getReports().get(0).getError());

    }

    @Test
    public void testForCorrectSpreadsheet() {
        List<Sheet> objectSheets = new ArrayList<>();
        try {
            objectSheets = objectMapper.readValue(correctSheets, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        spreadsheet.setResults(objectSheets);

        ValidatorResponse responseDto = service.verifySpreadsheet(spreadsheet);
        assertEquals("All results are correct. " +
                "You can clean up and submit your source code now. " +
                "Your passcode: place-for-the-passcode", responseDto.getMessage());
    }

    @Test
    public void testForSpreadsheetWithTypeMistakes() {
        List<Sheet> objectSheets = new ArrayList<>();
        try {
            objectSheets = objectMapper.readValue(incorrectTypeSheets, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        spreadsheet.setResults(objectSheets);

        ValidatorResponse responseDto = service.verifySpreadsheet(spreadsheet);
        assertNull(responseDto.getMessage());

        List<Report> reports = responseDto.getReports();
        assertNotNull(reports);

        Optional<Report> optionalReport33 = reports.stream()
                .filter(rep -> rep.getId().equals("sheet-33"))
                .findFirst();
        assertTrue(optionalReport33.isPresent());
        Report report33 = optionalReport33.get();
        assertEquals("invalid result: invalid type in cell C1"
                        + ", should be " + Type.ERROR
                , report33.getError());

        Optional<Report> optionalReport23 = reports.stream()
                .filter(rep -> rep.getId().equals("sheet-23"))
                .findFirst();
        assertTrue(optionalReport23.isPresent());
        Report report23 = optionalReport23.get();
        assertEquals("invalid result: invalid type in cell D1"
                        + ", should be " + Type.STRING
                , report23.getError());

        Optional<Report> optionalReport17 = reports.stream()
                .filter(rep -> rep.getId().equals("sheet-17"))
                .findFirst();
        assertTrue(optionalReport17.isPresent());
        Report report17 = optionalReport17.get();
        assertEquals("invalid result: invalid type in cell A1"
                        + ", should be " + Type.BOOLEAN
                , report17.getError());

    }

    @Test
    public void testForSpreadsheetWithValueMistakes() {
        List<Sheet> objectSheets = new ArrayList<>();
        try {
            objectSheets = objectMapper.readValue(incorrectValueSheets, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        spreadsheet.setResults(objectSheets);

        ValidatorResponse responseDto = service.verifySpreadsheet(spreadsheet);
        assertNull(responseDto.getMessage());

        List<Report> reports = responseDto.getReports();
        assertNotNull(reports);

        Optional<Report> optionalReport33 = reports.stream()
                .filter(rep -> rep.getId().equals("sheet-33"))
                .findFirst();
        assertTrue(optionalReport33.isPresent());
        Report report33 = optionalReport33.get();
        assertEquals("invalid result: invalid value in cell A1"
                        + ", value " + 2 + " is incorrect"
                , report33.getError());

        Optional<Report> optionalReport19 = reports.stream()
                .filter(rep -> rep.getId().equals("sheet-19"))
                .findFirst();
        assertTrue(optionalReport19.isPresent());
        Report report19 = optionalReport19.get();
        assertEquals("invalid result: invalid value in cell A1"
                        + ", value " + "Hello,World!" + " is incorrect"
                , report19.getError());

        Optional<Report> optionalReport10 = reports.stream()
                .filter(rep -> rep.getId().equals("sheet-10"))
                .findFirst();
        assertTrue(optionalReport10.isPresent());
        Report report10 = optionalReport10.get();
        assertEquals("invalid result: invalid value in cell C1"
                        + ", value " + true + " is incorrect"
                , report10.getError());

    }

}