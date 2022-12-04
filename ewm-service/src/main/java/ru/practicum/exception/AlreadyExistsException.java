package ru.practicum.exception;

/**
 * Такой объект уже существует
 */
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }

}
