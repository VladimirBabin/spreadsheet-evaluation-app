package com.vladimirbabin.wixgrow.spreadsheet_evaluator_swagger.service;


import com.vladimirbabin.wixgrow.spreadsheet_evaluator_swagger.AppProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SwaggerService {
    private final WebClient client;
    private final AppProperties properties;

    public SwaggerService(AppProperties properties) {
        this.client = WebClient.builder().build();
        this.properties = properties;
    }

    public String getAndEvaluateSpreadsheet() {
        return client
                .get()
                .uri(properties.getUrlForSpreadsheetEvaluationService())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
