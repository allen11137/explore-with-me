package ru.practicum.exception;

public class ParticipantStatusException extends RuntimeException {
    public ParticipantStatusException(String message) {
        super(message);
    }
}