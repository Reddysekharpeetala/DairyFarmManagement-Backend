package com.example.demo.dto;

import java.time.LocalDate;

public class ActivityResponse {

    private String type;
    private String message;
    private String icon;
    private LocalDate date;

    public ActivityResponse() {
    }

    public ActivityResponse(String type, String message, String icon, LocalDate date) {
        this.type = type;
        this.message = message;
        this.icon = icon;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}