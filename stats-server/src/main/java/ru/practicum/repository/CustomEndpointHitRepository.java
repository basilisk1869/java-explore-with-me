package ru.practicum.repository;

import org.springframework.lang.Nullable;
import ru.practicum.dto.ViewStats;

import javax.validation.constraints.NotNull;
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
    @NotNull List<ViewStats> getViewStats(
            @NotNull LocalDateTime start,
            @NotNull LocalDateTime end,
            @Nullable List<String> uris,
            @Nullable Boolean unique);

}
