package ru.practicum.event.service;

import org.springframework.lang.Nullable;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventFullDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    /**
     * Получение событий по заданным условиям
     * @param userIds Список пользователей, чьи события нужно найти (null - неважно)
     * @param stateIds Список состояний в которых находятся искомые события (null - неважно)
     * @param categoryIds Список id категорий в которых будет вестись поиск (null - неважно)
     * @param rangeStart Дата и время не раньше которых должно произойти событие (null - неважно)
     * @param rangeEnd Дата и время не позже которых должно произойти событие (null - неважно)
     * @param from Количество событий, которые нужно пропустить для формирования текущего набора
     * @param size Количество событий в наборе
     * @return Список полных DTO событий
     */
    @NotNull List<EventFullDto> getEvents(@Nullable List<Long> userIds,
                                          @Nullable List<String> stateIds,
                                          @Nullable List<Long> categoryIds,
                                          @Nullable LocalDateTime rangeStart,
                                          @Nullable LocalDateTime rangeEnd,
                                          int from,
                                          int size);

    /**
     * Обновление события
     * @param eventId Идентификатор события
     * @param adminUpdateEventRequest Новые данные события
     * @return Обновленное событие
     */
    @NotNull EventFullDto putEvent(long eventId, @NotNull AdminUpdateEventRequest adminUpdateEventRequest);

    /**
     * Опубликование события
     * @param eventId Идентификатор события
     * @return Обновленное событие
     */
    @NotNull EventFullDto publishEvent(long eventId);

    /**
     * Отклонение публикации события
     * @param eventId Идентификатор события
     * @return Обновленное событие
     */
    @NotNull EventFullDto rejectEvent(long eventId);

}
