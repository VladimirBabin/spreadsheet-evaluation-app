package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "spreadsheet-evaluator")
public class AppProperties {

    @NotBlank
    private String urlForSpreadsheetEvaluationService;

    @NotBlank
    private String urlForBrokenEvaluationService;
    public String getUrlForSpreadsheetEvaluationService() {
        return urlForSpreadsheetEvaluationService;
    }

    public void setUrlForSpreadsheetEvaluationService(String urlForSpreadsheetEvaluationService) {
        this.urlForSpreadsheetEvaluationService = urlForSpreadsheetEvaluationService;
    }
    public String getUrlForBrokenEvaluationService() {
        return urlForBrokenEvaluationService;
    }

    public void setUrlForBrokenEvaluationService(String urlForBrokenEvaluationService) {
        this.urlForBrokenEvaluationService = urlForBrokenEvaluationService;
    }
}
