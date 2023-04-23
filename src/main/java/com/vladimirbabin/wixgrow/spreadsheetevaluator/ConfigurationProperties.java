package com.vladimirbabin.wixgrow.spreadsheetevaluator;

@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "spreadsheet")
public record ConfigurationProperties(String getUrlForGettingTheTask, String getEmailForResulSubmission) {
}
