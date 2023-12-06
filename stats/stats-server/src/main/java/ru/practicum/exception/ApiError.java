package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
    private final List<String> errors;
    private final String message;
    private final String cause;
    private final HttpStatus status;
    private final String timestamp;
}
