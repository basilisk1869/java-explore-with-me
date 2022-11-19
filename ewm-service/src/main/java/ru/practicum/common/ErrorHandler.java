package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAccessDenied(final AccessDeniedException exception) {
        logException(exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAlreadyExists(final AlreadyExistsException exception) {
        logException(exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(final NotFoundException exception) {
        logException(exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String>  handleIllegalArgument(final IllegalArgumentException exception) {
        logException(exception);
        if (exception.getMessage() != null) {
            return Map.of("error", exception.getMessage());
        } else {
            return Map.of();
        }
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
    }

}
