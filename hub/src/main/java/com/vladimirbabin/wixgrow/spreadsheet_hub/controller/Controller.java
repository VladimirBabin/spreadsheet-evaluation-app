package com.vladimirbabin.wixgrow.spreadsheet_hub.controller;

import com.vladimirbabin.wixgrow.spreadsheet_hub.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final AppProperties properties;

    public Controller(AppProperties properties) {
        this.properties = properties;
    }

    @GetMapping(value = "/sheets",
                produces = "application/json")
    public String getSheets() {
        return properties.getSheets();
    }


}
