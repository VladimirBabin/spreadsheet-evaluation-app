package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.controller;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.SpreadsheetToVerify;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.VerificationResponse;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling.WrongResult;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    private final VerificationService service;

    public Controller(VerificationService service) {
        this.service = service;
    }

    @PostMapping("/verify")
    public VerificationResponse verifySpreadsheet(@RequestBody SpreadsheetToVerify spreadsheet) {
        VerificationResponse responseDto = service.verifySpreadsheet(spreadsheet);
        if (responseDto.getMessage() != null) {
            return responseDto;
        }
        throw new WrongResult(responseDto.getReports().toString());
    }
}
