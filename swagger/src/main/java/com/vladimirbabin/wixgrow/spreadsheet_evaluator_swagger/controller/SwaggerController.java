package com.vladimirbabin.wixgrow.spreadsheet_evaluator_swagger.controller;

import com.vladimirbabin.wixgrow.spreadsheet_evaluator_swagger.service.SwaggerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SwaggerController {
    private final SwaggerService service;

    public SwaggerController(SwaggerService service) {
        this.service = service;
    }

    @GetMapping("/spreadsheet-evaluation")
    public String computeSpreadsheetEvaluation() {
        return service.getAndEvaluateSpreadsheet();
    }
}
