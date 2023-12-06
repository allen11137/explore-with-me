package ru.practicum.exception;

public class UserNameValidationException extends RuntimeException {
    public UserNameValidationException(String message) {
        super(message);
    }
}
