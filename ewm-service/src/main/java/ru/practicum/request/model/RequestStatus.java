package ru.practicum.request.model;

/**
 * Статус запроса на участие в событии
 */
public enum RequestStatus {
    /**
     * На модерации
     */
    PENDING,

    /**
     * Подтвержден
     */
    CONFIRMED,

    /**
     * Отклонен
     */
    REJECTED,

    /**
     * Отменен
     */
    CANCELED
}
