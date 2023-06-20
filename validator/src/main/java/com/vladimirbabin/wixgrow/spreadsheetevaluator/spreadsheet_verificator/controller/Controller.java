package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.controller;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.SpreadsheetToVerify;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.ValidatorResponse;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling.WrongResult;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service.ValidatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    private final ValidatorService service;

    public Controller(ValidatorService service) {
        this.service = service;
    }

    @PostMapping(value = "/validate",
                produces = "application/json")
    public ValidatorResponse verifySpreadsheet(@RequestBody SpreadsheetToVerify spreadsheet) {
        ValidatorResponse responseDto = service.verifySpreadsheet(spreadsheet);
        if (responseDto.getMessage() != null) {
            return responseDto;
        }
        throw new WrongResult(responseDto.getReports().toString());
    }
}
