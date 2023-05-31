package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "spreadsheet-evaluator")
public class AppProperties {

    @NotBlank
    private String urlForSpreadsheetEvaluationService;

    public String getUrlForSpreadsheetEvaluationService() {
        return urlForSpreadsheetEvaluationService;
    }

    public void setUrlForSpreadsheetEvaluationService(String urlForSpreadsheetEvaluationService) {
        this.urlForSpreadsheetEvaluationService = urlForSpreadsheetEvaluationService;
    }
}
