package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserEventService {

    /**
     * Получение событий их инициатором
     * @param userId Идентификатор пользователя
     * @param from Количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size Количество элементов в наборе
     * @return Список полных DTO событий
     */
    @NotNull List<EventFullDto> getEvents(long userId, int from, int size);

    /**
     * Изменение события
     * @param userId Идентификатор пользователя
     * @param updateEventRequest Новые данные о событии
     * @return Обновленное событие
     */
    @NotNull EventFullDto patchEvent(long userId, @NotNull UpdateEventRequest updateEventRequest);

    /**
     * Добавление события
     * @param userId Идентификатор пользователя
     * @param newEventDto Новое событие
     * @return Данные события
     */
    @NotNull EventFullDto postEvent(long userId, @NotNull NewEventDto newEventDto);

    /**
     * Получение события
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Данные события
     */
    @NotNull EventFullDto getEvent(long userId, long eventId);

    /**
     * Отмена события
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Обновленное событие
     */
    @NotNull EventFullDto cancelEvent(long userId, long eventId);

    /**
     * Получение запросов на участие в событии
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Список запросов на участие
     */
    @NotNull List<ParticipationRequestDto> getRequests(long userId, long eventId);

    /**
     * Подтверждение запроса на участие в событии
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @param reqId Идентификатор запроса
     * @return Обновленные данные запроса
     */
    @NotNull ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId);

    /**
     * Отклонение запроса на участие в событии
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @param reqId Идентификатор запроса
     * @return Обновленные данные запроса
     */
    @NotNull ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId);

}
