package ru.practicum.exception;

public class ResponseError {
    String error;
    String description;

    public ResponseError(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
