package ru.practicum.repository;

import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEndpointHitRepository {

    /**
     * Получение статистики по посещениям.
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику
     * @param end Дата и время конца диапазона за который нужно выгрузить статистику
     * @param uris Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения
     * @return Список {@link ViewStats}
     */
    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
