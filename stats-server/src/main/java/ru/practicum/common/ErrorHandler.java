package ru.practicum.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Обработка исключений
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(final IllegalArgumentException exception) {
        logException(exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(final HttpMediaTypeNotAcceptableException exception) {
        logException(exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(final RuntimeException exception) {
        logException(exception);
        return exception.getMessage();
    }

    private void logException(final Exception exception) {
        if (exception.getMessage() != null) {
            log.error(exception.getMessage());
        }
        exception.printStackTrace();
    }

}
