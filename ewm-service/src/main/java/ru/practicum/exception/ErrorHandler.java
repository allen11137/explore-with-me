package ru.practicum.exception;

import io.micrometer.core.lang.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.event.Constant.DATA_TIME_PATTERN;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorApi> handle(Exception ex) throws IOException {
        ErrorApi apiError = getApiError(Collections.singletonList(error(ex)), "Запрос неправильно составлен.", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    public @NotNull
    ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField());
        }
        ErrorApi apiError = getApiError(errors, "Запрашиваемый объект не найден.", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorApi> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        ErrorApi apiError = getApiError(errors, "Запрашиваемый объект не найден.", ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    private String error(Exception e) throws IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace();
        String error = sw.toString();
        sw.close();
        pw.close();
        return error;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)   // для всех ситуаций, если искомый объект не найден
    public ErrorApi handle(final NotFoundException e) throws IOException {
        return getApiError(error(e), HttpStatus.NOT_FOUND, "Запрашиваемый объект не найден.", e.getLocalizedMessage());
    }

    @ExceptionHandler(CategoryValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)   // Категория существует
    public ErrorApi handle(final CategoryValidationException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Для запрошенной операции не выполнены условия.", e.getLocalizedMessage());
    }

    @ExceptionHandler(DoubleNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  //если есть дубликат Name.
    public ErrorApi handleThrowable(final DoubleNameException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Имя уже существует.", e.getLocalizedMessage());
    }

    @ExceptionHandler(DoubleEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  //если есть дубликат Email.
    public ErrorApi handleThrowable(final DoubleEmailException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Email уже существует.", e.getLocalizedMessage());
    }

    @ExceptionHandler(DateEventException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleThrowable(final DateEventException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Неверное время.", e.getLocalizedMessage());
    }

    @ExceptionHandler(ArgumentStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleThrowable(final ArgumentStateException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Запрашиваемый объект не найден.", e.getLocalizedMessage());
    }

    @ExceptionHandler(OverflowLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleThrowable(final OverflowLimitException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Достигнут лимит участников", e.getLocalizedMessage());
    }

    @ExceptionHandler(ParticipantStatusException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleThrowable(final ParticipantStatusException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Статус запроса: NOT PENDING", e.getLocalizedMessage());
    }

    @ExceptionHandler(DoubleParticipationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleThrowable(final DoubleParticipationException e) throws IOException {
        return getApiError(error(e), HttpStatus.CONFLICT, "Запрос уже был создан", e.getLocalizedMessage());
    }

    private static ErrorApi getApiError(List<String> errors, String reason, String ex) {
        return new ErrorApi()
                .setErrors(errors)
                .setStatus(HttpStatus.BAD_REQUEST)
                .setReason(reason)
                .setMessage(ex)
                .setTimestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)));
    }

    private ErrorApi getApiError(String e, HttpStatus conflict, String requestHasBeen, String e1) {
        return new ErrorApi()
                .setErrors(Collections.singletonList(e))
                .setStatus(conflict)
                .setReason(requestHasBeen)
                .setMessage(e1)
                .setTimestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)));
    }
}