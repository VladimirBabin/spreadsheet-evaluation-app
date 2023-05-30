package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Report;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.SpreadsheetToVerify;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.VerificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VerificationServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InputChecker inputChecker = new InputChecker();
    private final ReportWriter reportWriter = new ReportWriter(inputChecker);
    private final VerificationService service = new VerificationService(reportWriter);
    private SpreadsheetToVerify spreadsheet;

    @BeforeEach
    public void setUp() {
        spreadsheet = new SpreadsheetToVerify();
    }

    @Test
    public void testForNullSpreadsheet() {
        VerificationResponse responseDto = service.verifySpreadsheet(null);
        assertNull(responseDto.getMessage());
        assertEquals("invalid  result: " +
                        "the spreadsheet can't be null"
                , responseDto.getReports().get(0).getError());
    }

    @Test
    public void testForEmptySpreadsheet() {
        List<Sheet> objectSheets = new ArrayList<>();
        spreadsheet.setSheets(objectSheets);

        VerificationResponse responseDto = service.verifySpreadsheet(spreadsheet);
        assertNull(responseDto.getMessage());
        assertEquals("invalid  result: " +
                        "the spreadsheet can't be empty"
                , responseDto.getReports().get(0).getError());

    }

    @Test
    public void testForCorrectSpreadsheet() {
        List<Sheet> objectSheets = new ArrayList<>();
        try {
            objectSheets = objectMapper.readValue(new File(
                            "C:\\Users\\mi\\Spreadsheet_Verificator\\src\\main\\resources\\correct-sheets.json")
                    , new TypeReference<>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        spreadsheet.setSheets(objectSheets);

        VerificationResponse responseDto = service.verifySpreadsheet(spreadsheet);
        assertEquals("All results are correct. " +
                "You can clean up and submit your source code now. " +
                "Your passcode: place-for-the-passcode", responseDto.getMessage());
    }

    @Test
    public void testForSpreadsheetWithTypeMistakes() {
        List<Sheet> objectSheets = new ArrayList<>();
        try {
            objectSheets = objectMapper.readValue(new File(
                            "C:\\Users\\mi\\Spreadsheet_Verificator\\src\\main\\resources\\incorrect-type-sheets.json")
                    , new TypeReference<>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        spreadsheet.setSheets(objectSheets);

        VerificationResponse responseDto = service.verifySpreadsheet(spreadsheet);
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
            objectSheets = objectMapper.readValue(new File(
                            "C:\\Users\\mi\\Spreadsheet_Verificator\\src\\main\\resources\\incorrect-value-sheets.json")
                    , new TypeReference<>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        spreadsheet.setSheets(objectSheets);

        VerificationResponse responseDto = service.verifySpreadsheet(spreadsheet);
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