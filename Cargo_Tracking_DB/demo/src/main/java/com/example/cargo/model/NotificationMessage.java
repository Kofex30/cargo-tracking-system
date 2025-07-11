package com.example.cargo.model;

public class NotificationMessage {
    private String message;

    public NotificationMessage() {}

    public NotificationMessage(String message) {
        this.message = message;
    }

    // Getter & Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
