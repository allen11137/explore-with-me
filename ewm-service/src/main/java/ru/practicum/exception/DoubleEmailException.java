package ru.practicum.exception;

public class DoubleEmailException extends RuntimeException {
    public DoubleEmailException(String message) {
        super(message);
    }
}

