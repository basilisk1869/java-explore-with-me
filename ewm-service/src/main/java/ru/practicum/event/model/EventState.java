package ru.practicum.event.model;

/**
 * Состояние публикации события
 */
public enum EventState {
    /**
     * На модерации
     */
    PENDING,

    /**
     * Опубликовано
     */
    PUBLISHED,

    /**
     * Отменено
     */
    CANCELED
}
