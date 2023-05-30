package com.vladimirbabin.wixgrow.spreadsheetevaluator.controller;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Spreadsheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.SpreadsheetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    private final SpreadsheetClient client;

    public Controller(SpreadsheetClient client) {
        this.client = client;
    }

    @GetMapping("/spreadsheet-evaluation")
    public String computeSpreadsheetEvaluation() {
        Spreadsheet toEvaluate = client.getSpreadsheet();
        logger.info(toEvaluate.toString());
        client.evaluateAndSendSpreadsheet(toEvaluate);
        return client.getResponseMessage();
    }

}
