package ru.practicum.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDenied(final AccessDeniedException exception) {
        logException(exception);
        return makeApiError(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(final AlreadyExistsException exception) {
        logException(exception);
        return makeApiError(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(final NotFoundException exception) {
        logException(exception);
        return makeApiError(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        logException(exception);
        return makeApiError(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleException(final HttpMediaTypeNotAcceptableException exception) {
        logException(exception);
        return makeApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(final RuntimeException exception) {
        logException(exception);
        return makeApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private void logException(final Exception exception) {
        if (exception.getMessage() != null) {
            log.error(exception.getMessage());
        }
    }

    private ResponseEntity<String> makeApiError(HttpStatus httpStatus, Exception exception) {
        ApiError apiError = ApiError.builder()
                .status(httpStatus.toString())
                .message(exception.getMessage())
                .errors(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
        try {
            String apiErrorBody = objectMapper.writeValueAsString(apiError);
            log.error(apiErrorBody);
            return ResponseEntity.status(httpStatus).body(apiErrorBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
