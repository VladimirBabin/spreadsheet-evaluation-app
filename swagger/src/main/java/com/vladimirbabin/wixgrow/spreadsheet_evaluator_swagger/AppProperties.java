package com.vladimirbabin.wixgrow.spreadsheet_evaluator_swagger;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
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
