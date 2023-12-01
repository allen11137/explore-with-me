package ru.practicum.exception;

public class ArgumentStateException extends RuntimeException {
    public ArgumentStateException(String message) {
        super(message);
    }
}