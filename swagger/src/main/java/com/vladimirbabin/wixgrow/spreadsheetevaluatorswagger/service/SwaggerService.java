package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.service;


import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.AppProperties;
import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.dto.BadRequestDto;
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

    public String getAndEvaluateSpreadsheet(String url) {
        return client
                .get()
                .uri(url)
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(BadRequestDto.class)
                                .flatMap(entity -> Mono.error(new WrongResponseException(entity.getMessage()))))
                .onStatus(
                        HttpStatus.BAD_GATEWAY::equals,
                        response -> response.bodyToMono(BadRequestDto.class)
                                .flatMap(entity -> Mono.error(new WrongResponseException(entity.getMessage()))))
                .bodyToMono(String.class)
                .block();
    }

    public String getAndEvaluateBrokenSpreadsheet() {
        return getAndEvaluateSpreadsheet(properties.getUrlForBrokenEvaluationService());
    }

    public String getAndEvaluateWorkingSpreadsheet() {
        return getAndEvaluateSpreadsheet(properties.getUrlForSpreadsheetEvaluationService());
    }
}
