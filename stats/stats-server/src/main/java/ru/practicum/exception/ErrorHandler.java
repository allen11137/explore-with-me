package ru.practicum.exception;

import io.micrometer.core.lang.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.mapper.MapperOfStats.DATA_TIME_PATTERN;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiError> handle(Exception ex) {
        ApiError apiError = ApiError.builder()
                .errors(Collections.singletonList(ex.getLocalizedMessage()))
                .status(HttpStatus.BAD_REQUEST)
                .cause("Запрос составлен неправильно.")
                .message(ex.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    public @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                        @NonNull HttpHeaders headers,
                                                                        @NonNull HttpStatus status,
                                                                        @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField());
        }
        ApiError apiError = ApiError.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .cause("Запрашиваемый объект не найден.")
                .message(ex.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleViolaitionConstraint(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(v -> errors.add(v.getMessage()));
        ApiError apiError = ApiError.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .cause("Запрашиваемый объект не найден.")
                .message(ex.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}