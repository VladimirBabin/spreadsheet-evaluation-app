package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.controller;

import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.service.SwaggerService;
import org.springframework.http.ResponseEntity;
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
    public String computeWorkingSpreadsheetEvaluation() {
        return service.getAndEvaluateWorkingSpreadsheet();
    }

    @GetMapping("/broken-spreadsheet-evaluation")
    public String computeBrokenSpreadsheetEvaluation() {
        return service.getAndEvaluateBrokenSpreadsheet();
    }
}
