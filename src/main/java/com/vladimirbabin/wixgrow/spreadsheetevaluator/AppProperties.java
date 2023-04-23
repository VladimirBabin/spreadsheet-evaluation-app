package com.vladimirbabin.wixgrow.spreadsheetevaluator;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "spreadsheet")
public class AppProperties {

    @NotBlank
    private String urlForGettingTheTask;

    @Email
    private String emailForResultSubmission;


    public String getEmailForResultSubmission() {
        return emailForResultSubmission;
    }

    public void setEmailForResultSubmission(String emailForResultSubmission) {
        this.emailForResultSubmission = emailForResultSubmission;
    }

    public void setUrlForGettingTheTask(String urlForGettingTheTask) {
        this.urlForGettingTheTask = urlForGettingTheTask;
    }

    public String getUrlForGettingTheTask() {
        return urlForGettingTheTask;
    }
}
