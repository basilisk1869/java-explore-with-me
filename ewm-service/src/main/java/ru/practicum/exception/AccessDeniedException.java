package ru.practicum.exception;

/**
 * Действие запрещено
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }

}
