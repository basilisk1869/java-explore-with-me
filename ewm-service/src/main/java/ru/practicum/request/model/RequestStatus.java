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
     * Подтвежден
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
