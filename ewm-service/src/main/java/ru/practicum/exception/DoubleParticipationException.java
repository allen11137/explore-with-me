package ru.practicum.exception;

public class DoubleParticipationException extends RuntimeException {
    public DoubleParticipationException(String message) {
        super(message);
    }
}