package ru.practicum.exception;

/**
 * Объект не найден
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
