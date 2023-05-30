package com.vladimirbabin.wixgrow.spreadsheet_hub;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "hub")
public class AppProperties {

    @NotBlank
    private String sheets;

    public String getSheets() {
        return sheets;
    }

    public void setSheets(String sheets) {
        this.sheets = sheets;
    }
}
