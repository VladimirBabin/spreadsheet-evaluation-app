package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.service;


import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.AppProperties;
import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.exception_handling.WrongResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new WrongResponseException(errorBody))))
                .onStatus(
                        HttpStatus.BAD_GATEWAY::equals,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new WrongResponseException(errorBody))))
                .bodyToMono(String.class)
                .block();
    }
}
