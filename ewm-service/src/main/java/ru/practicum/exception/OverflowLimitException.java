package ru.practicum.exception;

public class OverflowLimitException extends RuntimeException {
    public OverflowLimitException(String message) {
        super(message);
    }
}