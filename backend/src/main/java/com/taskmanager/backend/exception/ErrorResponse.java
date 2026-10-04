package com.taskmanager.backend.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public class ErrorResponse {

    private OffsetDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> fieldErrors;

    public ErrorResponse(OffsetDateTime timestamp, int status, String message, Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}