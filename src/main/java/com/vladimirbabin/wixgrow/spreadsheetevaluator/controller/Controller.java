package com.vladimirbabin.wixgrow.spreadsheetevaluator.controller;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Spreadsheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.SpreadsheetClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final SpreadsheetClient client;

    public Controller(SpreadsheetClient client) {
        this.client = client;
    }

    @GetMapping("/spreadsheet-evaluation")
    public String computeSpreadsheetEvaluation() {
        Spreadsheet evaluatedSpreadsheet = client.getSpreadsheet();
        client.sendEvaluatedSpreadsheetAndLogResult(evaluatedSpreadsheet);
        return client.getResponseMessage();
    }

}
