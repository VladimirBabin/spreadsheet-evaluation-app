package com.vladimirbabin.wixgrow.spreadsheetevaluator.controller;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Spreadsheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.RestSpreadsheetClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final RestSpreadsheetClient client;

    public Controller(RestSpreadsheetClient client) {
        this.client = client;
    }

    @GetMapping("/call-app")
    public String callComputeSpreadsheet() {
        Spreadsheet resultSpreadsheet = client.getSpreadsheet();
        client.sendEvaluatedSpreadsheetAndLogResult(resultSpreadsheet);
        return client.getResponseMessage();
    }

}
