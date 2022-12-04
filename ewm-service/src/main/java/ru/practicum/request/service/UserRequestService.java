package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserRequestService {

    /**
     * Получение запросов на участие для пользователя
     * @param userId Идентификатор пользователя
     * @return Список запросов на участие
     */
    @NotNull List<ParticipationRequestDto> getRequests(long userId);

    /**
     * Создание запроса на участие в событии
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Созданный запрос на участие
     */
    @NotNull ParticipationRequestDto postRequest(long userId, long eventId);

    /**
     * Отменяет запрос на участие
     * @param userId Идентификатор пользователя
     * @param eventId Идентификатор события
     * @return Отмененный запрос на участие
     */
    @NotNull ParticipationRequestDto cancelRequest(long userId, long eventId);

}
