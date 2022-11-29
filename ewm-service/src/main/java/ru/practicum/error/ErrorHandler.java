package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.ApiError;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleAccessDenied(final AccessDeniedException exception) {
        logException(exception);
        return makeApiError(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleAlreadyExists(final AlreadyExistsException exception) {
        logException(exception);
        return makeApiError(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFound(final NotFoundException exception) {
        logException(exception);
        return makeApiError(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleIllegalArgument(final IllegalArgumentException exception) {
        logException(exception);
        return makeApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(final HttpMediaTypeNotAcceptableException exception) {
        logException(exception);
        return makeApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleException(final RuntimeException exception) {
        logException(exception);
        return makeApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private void logException(final Exception exception) {
        if (exception.getMessage() != null) {
            log.error(exception.getMessage());
        }
        exception.printStackTrace();
    }

    private ResponseEntity<ApiError> makeApiError(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(ApiError.builder()
                .status(httpStatus.toString())
                .message(message)
                .build());
    }

}